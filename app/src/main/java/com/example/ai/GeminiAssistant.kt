package com.example.ai

import com.example.BuildConfig
import com.example.data.model.PriceRecordEntity
import com.example.data.model.ProductEntity
import com.example.localization.AppLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiAssistant {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun answerQuestion(
        question: String,
        products: List<ProductEntity>,
        prices: List<PriceRecordEntity>,
        language: AppLanguage
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        // Build verified database knowledge context
        val contextSummary = StringBuilder()
        contextSummary.append("VERIFIED MOROCCAN FOOD DATABASE CONTEXT:\n")
        contextSummary.append("Products:\n")
        products.forEach { p ->
            contextSummary.append("- [ID: ${p.id}] ${p.nameEn} | ${p.nameAr} | ${p.nameFr} | Category: ${p.category} | Origin: ${p.countryOfOrigin}, ${p.region} | Barcode: ${p.barcode}\n")
            if (p.ingredients.isNotEmpty()) contextSummary.append("  Ingredients: ${p.ingredients}\n")
            if (p.nutritionFacts.isNotEmpty()) contextSummary.append("  Nutrition: ${p.nutritionFacts}\n")
            if (p.manufacturer.isNotEmpty()) contextSummary.append("  Manufacturer: ${p.manufacturer} (${p.manufacturingLocation})\n")
        }

        contextSummary.append("\nVerified Market Prices by City:\n")
        prices.filter { it.price != null }.forEach { pr ->
            val product = products.find { it.id == pr.productId }
            val prodName = product?.nameAr ?: product?.nameEn ?: pr.productId
            contextSummary.append("- ${prodName} in ${pr.city}: ${pr.price} ${pr.currency}/${pr.unit} (Source: ${pr.sourceName}, Date: ${pr.date}, Status: ${pr.verificationStatus})\n")
        }

        if (apiKey.isNullOrEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            // Intelligent offline deterministic answer directly from database
            return@withContext answerOffline(question, products, prices, language)
        }

        try {
            val systemPrompt = """
                You are the official AI Assistant for Morocco Food Prices (أسعار المواد الغذائية بالمغرب).
                MANDATORY RULES:
                1. Only use the verified prices and product facts provided in the DATABASE CONTEXT below.
                2. If a price or product is not in the database context, explicitly state: 'Price data is not available yet in the official database' (or in Arabic 'بيانات السعر غير متوفرة بعد في السجل المعتمد').
                3. NEVER invent, fabricate, or guess food prices under any circumstances.
                4. Answer in the same language as the user question (Arabic, French, or English).
                5. Always quote the city, source, and date when providing a price.
                
                $contextSummary
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", question))
                        })
                    })
                })
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", systemPrompt))
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.2) // Low temperature for high factual accuracy
                })
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val resStr = response.body?.string() ?: ""
                val resObj = JSONObject(resStr)
                val candidates = resObj.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text")
                    }
                }
            }
            return@withContext answerOffline(question, products, prices, language)
        } catch (e: Exception) {
            return@withContext answerOffline(question, products, prices, language)
        }
    }

    private fun answerOffline(
        query: String,
        products: List<ProductEntity>,
        prices: List<PriceRecordEntity>,
        language: AppLanguage
    ): String {
        val q = query.lowercase()

        // Match product
        val matchedProduct = products.find { p ->
            q.contains(p.nameAr.lowercase()) ||
            q.contains(p.nameEn.lowercase()) ||
            q.contains(p.nameFr.lowercase()) ||
            (p.category.equals("Vegetables", true) && (q.contains("خضر") || q.contains("légume") || q.contains("vegetable"))) ||
            (p.id.contains("tomato") && (q.contains("طماطم") || q.contains("tomat") || q.contains("ماتيشة"))) ||
            (p.id.contains("potato") && (q.contains("بطاطس") || q.contains("pomme de terre") || q.contains("بطاطا"))) ||
            (p.id.contains("sardine") && (q.contains("سردين") || q.contains("sardine"))) ||
            (p.id.contains("argan") && (q.contains("أركان") || q.contains("argan"))) ||
            (p.id.contains("olive") && (q.contains("زيت") || q.contains("olive"))) ||
            (p.id.contains("saffron") && (q.contains("زعفران") || q.contains("safran"))) ||
            (p.id.contains("dates") && (q.contains("تمر") || q.contains("datte"))) ||
            (p.id.contains("chicken") && (q.contains("دجاج") || q.contains("poulet"))) ||
            (p.id.contains("beef") && (q.contains("لحم") || q.contains("viande") || q.contains("بقر")))
        }

        if (matchedProduct != null) {
            val productPrices = prices.filter { it.productId == matchedProduct.id && it.price != null }
            if (productPrices.isNotEmpty()) {
                val sb = StringBuilder()
                when (language) {
                    AppLanguage.ARABIC -> {
                        sb.append("📋 الأسعار المعتمدة لـ ${matchedProduct.nameAr}:\n\n")
                        productPrices.forEach { pr ->
                            sb.append("• في مدينة ${pr.city}: ${pr.price} درهم / ${pr.unit}\n")
                            sb.append("  المصدر: ${pr.sourceName} (${pr.date})\n")
                        }
                        if (matchedProduct.nutritionFacts.isNotEmpty()) {
                            sb.append("\n🥗 القيمة الغذائية: ${matchedProduct.nutritionFacts}\n")
                        }
                        if (matchedProduct.ingredients.isNotEmpty()) {
                            sb.append("🧂 المكونات: ${matchedProduct.ingredients}\n")
                        }
                    }
                    AppLanguage.FRENCH -> {
                        sb.append("📋 Prix officiels pour ${matchedProduct.nameFr} :\n\n")
                        productPrices.forEach { pr ->
                            sb.append("• À ${pr.city} : ${pr.price} MAD / ${pr.unit}\n")
                            sb.append("  Source : ${pr.sourceName} (${pr.date})\n")
                        }
                        if (matchedProduct.nutritionFacts.isNotEmpty()) {
                            sb.append("\n🥗 Valeur nutritionnelle : ${matchedProduct.nutritionFacts}\n")
                        }
                    }
                    AppLanguage.ENGLISH -> {
                        sb.append("📋 Verified official prices for ${matchedProduct.nameEn}:\n\n")
                        productPrices.forEach { pr ->
                            sb.append("• In ${pr.city}: ${pr.price} MAD / ${pr.unit}\n")
                            sb.append("  Source: ${pr.sourceName} (${pr.date})\n")
                        }
                        if (matchedProduct.nutritionFacts.isNotEmpty()) {
                            sb.append("\n🥗 Nutrition facts: ${matchedProduct.nutritionFacts}\n")
                        }
                    }
                }
                return sb.toString()
            } else {
                return when (language) {
                    AppLanguage.ARABIC -> "بيانات السعر لمنتج ${matchedProduct.nameAr} غير متوفرة بعد في السجل المعتمد. لم يتم ربط مصدر سعري مباشر له."
                    AppLanguage.FRENCH -> "Les données de prix pour ${matchedProduct.nameFr} ne sont pas encore disponibles dans la base officielle."
                    AppLanguage.ENGLISH -> "Price data for ${matchedProduct.nameEn} is not available yet in the verified database."
                }
            }
        }

        // Generic reply
        return when (language) {
            AppLanguage.ARABIC -> "مرحبًا بك في المساعد الذكي لأسعار المواد الغذائية بالمغرب. يمكنك سؤالي عن أسعار الطماطم، البطاطس، السردين، زيت أركان، الدجاج أو مقارنة الأسعار بين الدار البيضاء ومراكش والرباط. يعتمد النظام حصرًا على الأسعار الموثقة."
            AppLanguage.FRENCH -> "Bienvenue sur l'assistant IA des Prix Alimentaires Maroc. Vous pouvez poser des questions sur les tomates, sardines, huile d'argan, ou comparer les prix entre Casablanca, Rabat, Marrakech et Agadir."
            AppLanguage.ENGLISH -> "Welcome to the Morocco Food Prices AI Assistant. Ask about verified prices for tomatoes, potatoes, sardines, argan oil, or compare cities such as Casablanca, Rabat, and Marrakech."
        }
    }
}
