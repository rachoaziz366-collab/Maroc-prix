package com.example.localization

import androidx.compose.ui.unit.LayoutDirection

enum class AppLanguage(val code: String, val displayName: String, val layoutDirection: LayoutDirection) {
    ARABIC("ar", "العربية", LayoutDirection.Rtl),
    FRENCH("fr", "Français", LayoutDirection.Ltr),
    ENGLISH("en", "English", LayoutDirection.Ltr)
}

object AppStrings {
    private val translations = mapOf(
        // App General
        "app_name" to mapOf(
            AppLanguage.ARABIC to "أسعار المواد الغذائية بالمغرب",
            AppLanguage.FRENCH to "Prix Alimentaires Maroc",
            AppLanguage.ENGLISH to "Morocco Food Prices"
        ),
        "tagline" to mapOf(
            AppLanguage.ARABIC to "المنصة الوطنية لأسعار المنتجات والسلع الغذائية بالمغرب",
            AppLanguage.FRENCH to "Plateforme nationale des prix et produits alimentaires au Maroc",
            AppLanguage.ENGLISH to "National Moroccan food price and product intelligence platform"
        ),
        "search_hint" to mapOf(
            AppLanguage.ARABIC to "ابحث عن منتج، خضار، فواكه، زيت، أو كود بار...",
            AppLanguage.FRENCH to "Rechercher un produit, légume, huile ou code-barres...",
            AppLanguage.ENGLISH to "Search for a product, vegetable, oil or barcode..."
        ),
        "home" to mapOf(
            AppLanguage.ARABIC to "الرئيسية",
            AppLanguage.FRENCH to "Accueil",
            AppLanguage.ENGLISH to "Home"
        ),
        "terroir" to mapOf(
            AppLanguage.ARABIC to "منتجات مجالية",
            AppLanguage.FRENCH to "Produits du Terroir",
            AppLanguage.ENGLISH to "Local Terroir"
        ),
        "packaged" to mapOf(
            AppLanguage.ARABIC to "أغذية معلبة",
            AppLanguage.FRENCH to "Produits Emballés",
            AppLanguage.ENGLISH to "Packaged Food"
        ),
        "compare" to mapOf(
            AppLanguage.ARABIC to "مقارنة المدن",
            AppLanguage.FRENCH to "Comparer les Villes",
            AppLanguage.ENGLISH to "City Compare"
        ),
        "favorites" to mapOf(
            AppLanguage.ARABIC to "المفضلة",
            AppLanguage.FRENCH to "Favoris",
            AppLanguage.ENGLISH to "Favorites"
        ),
        "scanner" to mapOf(
            AppLanguage.ARABIC to "ماسح الباركود",
            AppLanguage.FRENCH to "Scanner Code-barres",
            AppLanguage.ENGLISH to "Barcode Scanner"
        ),
        "ai_assistant" to mapOf(
            AppLanguage.ARABIC to "المساعد الذكي",
            AppLanguage.FRENCH to "Assistant IA",
            AppLanguage.ENGLISH to "AI Assistant"
        ),
        "admin" to mapOf(
            AppLanguage.ARABIC to "لوحة الإدارة",
            AppLanguage.FRENCH to "Administration",
            AppLanguage.ENGLISH to "Admin Dashboard"
        ),
        "settings" to mapOf(
            AppLanguage.ARABIC to "الإعدادات",
            AppLanguage.FRENCH to "Paramètres",
            AppLanguage.ENGLISH to "Settings"
        ),
        "categories" to mapOf(
            AppLanguage.ARABIC to "الأصناف والقطاعات",
            AppLanguage.FRENCH to "Catégories",
            AppLanguage.ENGLISH to "Categories"
        ),
        "todays_updates" to mapOf(
            AppLanguage.ARABIC to "تحديثات أسعار اليوم",
            AppLanguage.FRENCH to "Mises à jour du jour",
            AppLanguage.ENGLISH to "Today's Price Updates"
        ),
        "popular_products" to mapOf(
            AppLanguage.ARABIC to "المنتجات الأكثر طلباً",
            AppLanguage.FRENCH to "Produits populaires",
            AppLanguage.ENGLISH to "Popular Products"
        ),
        "price_increases" to mapOf(
            AppLanguage.ARABIC to "منتجات ارتفع سعرها",
            AppLanguage.FRENCH to "Hausses de prix",
            AppLanguage.ENGLISH to "Price Increases"
        ),
        "price_decreases" to mapOf(
            AppLanguage.ARABIC to "منتجات انخفض سعرها",
            AppLanguage.FRENCH to "Baisses de prix",
            AppLanguage.ENGLISH to "Price Decreases"
        ),
        "price_not_available" to mapOf(
            AppLanguage.ARABIC to "بيانات السعر غير متوفرة بعد",
            AppLanguage.FRENCH to "Données de prix non disponibles pour le moment",
            AppLanguage.ENGLISH to "Price data is not available yet"
        ),
        "verified_badge" to mapOf(
            AppLanguage.ARABIC to "مصدر معتمد وموثق",
            AppLanguage.FRENCH to "Source vérifiée",
            AppLanguage.ENGLISH to "Verified Source"
        ),
        "demo_data_badge" to mapOf(
            AppLanguage.ARABIC to "بيانات تجريبية (DEMO DATA)",
            AppLanguage.FRENCH to "Données démo (DEMO DATA)",
            AppLanguage.ENGLISH to "DEMO DATA"
        ),
        "select_city" to mapOf(
            AppLanguage.ARABIC to "اختر المدينة",
            AppLanguage.FRENCH to "Choisir la ville",
            AppLanguage.ENGLISH to "Select City"
        ),
        "current_price" to mapOf(
            AppLanguage.ARABIC to "السعر الحالي",
            AppLanguage.FRENCH to "Prix actuel",
            AppLanguage.ENGLISH to "Current Price"
        ),
        "unit" to mapOf(
            AppLanguage.ARABIC to "الوحدة",
            AppLanguage.FRENCH to "Unité",
            AppLanguage.ENGLISH to "Unit"
        ),
        "currency" to mapOf(
            AppLanguage.ARABIC to "درهم مغربي",
            AppLanguage.FRENCH to "MAD",
            AppLanguage.ENGLISH to "MAD"
        ),
        "price_history" to mapOf(
            AppLanguage.ARABIC to "سجل تتبع الأسعار",
            AppLanguage.FRENCH to "Historique des prix",
            AppLanguage.ENGLISH to "Price History"
        ),
        "price_sources" to mapOf(
            AppLanguage.ARABIC to "مصادر الأسعار المعتمدة",
            AppLanguage.FRENCH to "Sources de prix",
            AppLanguage.ENGLISH to "Price Sources"
        ),
        "ingredients" to mapOf(
            AppLanguage.ARABIC to "المكونات",
            AppLanguage.FRENCH to "Ingrédients",
            AppLanguage.ENGLISH to "Ingredients"
        ),
        "nutrition" to mapOf(
            AppLanguage.ARABIC to "القيمة الغذائية",
            AppLanguage.FRENCH to "Valeurs nutritionnelles",
            AppLanguage.ENGLISH to "Nutrition Facts"
        ),
        "allergens" to mapOf(
            AppLanguage.ARABIC to "مسببات الحساسية",
            AppLanguage.FRENCH to "Allergènes",
            AppLanguage.ENGLISH to "Allergens"
        ),
        "benefits" to mapOf(
            AppLanguage.ARABIC to "فوائد المنتج",
            AppLanguage.FRENCH to "Bienfaits du produit",
            AppLanguage.ENGLISH to "Product Benefits"
        ),
        "manufacturer" to mapOf(
            AppLanguage.ARABIC to "المُصنّع والتعاونية",
            AppLanguage.FRENCH to "Fabricant / Coopérative",
            AppLanguage.ENGLISH to "Manufacturer / Cooperative"
        ),
        "origin" to mapOf(
            AppLanguage.ARABIC to "بلد المنشأ",
            AppLanguage.FRENCH to "Pays d'origine",
            AppLanguage.ENGLISH to "Country of Origin"
        ),
        "location" to mapOf(
            AppLanguage.ARABIC to "موقع ومقر الإنتاج",
            AppLanguage.FRENCH to "Lieu de production",
            AppLanguage.ENGLISH to "Manufacturing Location"
        ),
        "barcode" to mapOf(
            AppLanguage.ARABIC to "رمز الباركود",
            AppLanguage.FRENCH to "Code-barres",
            AppLanguage.ENGLISH to "Barcode"
        ),
        "submit_price" to mapOf(
            AppLanguage.ARABIC to "المساهمة بسعر من مدينتك",
            AppLanguage.FRENCH to "Soumettre un prix",
            AppLanguage.ENGLISH to "Submit a Price"
        ),
        "submit_notice" to mapOf(
            AppLanguage.ARABIC to "تخضع الأسعار المقدمة للمراجعة والتحقق قبل اعتمادها رسميًا في النظام.",
            AppLanguage.FRENCH to "Les prix soumis sont soumis à modération avant validation officielle.",
            AppLanguage.ENGLISH to "Submitted prices are verified by moderators before becoming official."
        ),
        "price_alerts" to mapOf(
            AppLanguage.ARABIC to "تنبيهات الأسعار",
            AppLanguage.FRENCH to "Alertes de prix",
            AppLanguage.ENGLISH to "Price Alerts"
        ),
        "create_alert" to mapOf(
            AppLanguage.ARABIC to "إنشاء تنبيه جديد",
            AppLanguage.FRENCH to "Créer une alerte",
            AppLanguage.ENGLISH to "Create Alert"
        ),
        "alert_desc" to mapOf(
            AppLanguage.ARABIC to "أعلمني عندما ينخفض السعر عن:",
            AppLanguage.FRENCH to "Me notifier si le prix descend sous :",
            AppLanguage.ENGLISH to "Notify me when price drops below:"
        ),
        "language" to mapOf(
            AppLanguage.ARABIC to "لغة التطبيق",
            AppLanguage.FRENCH to "Langue de l'application",
            AppLanguage.ENGLISH to "Application Language"
        ),
        "user_role" to mapOf(
            AppLanguage.ARABIC to "صفة المستخدم الحالية",
            AppLanguage.FRENCH to "Rôle utilisateur actuel",
            AppLanguage.ENGLISH to "Current User Role"
        ),
        "analytics" to mapOf(
            AppLanguage.ARABIC to "إحصائيات المنظومة",
            AppLanguage.FRENCH to "Statistiques du système",
            AppLanguage.ENGLISH to "System Analytics"
        ),
        "moderation_queue" to mapOf(
            AppLanguage.ARABIC to "قائمة مراجعة مساهمات الأسعار",
            AppLanguage.FRENCH to "File de modération des prix",
            AppLanguage.ENGLISH to "Price Moderation Queue"
        ),
        "approve" to mapOf(
            AppLanguage.ARABIC to "قبول وتوثيق",
            AppLanguage.FRENCH to "Approuver et vérifier",
            AppLanguage.ENGLISH to "Approve & Verify"
        ),
        "reject" to mapOf(
            AppLanguage.ARABIC to "رفض",
            AppLanguage.FRENCH to "Rejeter",
            AppLanguage.ENGLISH to "Reject"
        ),
        "pending" to mapOf(
            AppLanguage.ARABIC to "قيد المراجعة",
            AppLanguage.FRENCH to "En attente",
            AppLanguage.ENGLISH to "Pending Review"
        ),
        "manage_products" to mapOf(
            AppLanguage.ARABIC to "إدارة قاعدة المنتجات",
            AppLanguage.FRENCH to "Gérer les produits",
            AppLanguage.ENGLISH to "Manage Products"
        ),
        "manage_sources" to mapOf(
            AppLanguage.ARABIC to "إدارة مصادر الأسعار وAPIs",
            AppLanguage.FRENCH to "Gérer les sources & APIs",
            AppLanguage.ENGLISH to "Manage Sources & APIs"
        ),
        "audit_logs" to mapOf(
            AppLanguage.ARABIC to "سجل العمليات والأمان",
            AppLanguage.FRENCH to "Journal d'audit & sécurité",
            AppLanguage.ENGLISH to "Audit Logs & Security"
        ),
        "ai_prompt_hint" to mapOf(
            AppLanguage.ARABIC to "اسأل عن أسعار الطماطم، اللحم، السردين، زيت أركان، أو قارن بين المدن...",
            AppLanguage.FRENCH to "Posez une question sur les prix, comparez deux villes...",
            AppLanguage.ENGLISH to "Ask about vegetable prices, compare Casablanca & Rabat..."
        ),
        "ai_disclaimer" to mapOf(
            AppLanguage.ARABIC to "يعتمد المساعد الذكي حصرًا على الأسعار المعتمدة والموثقة في قاعدة البيانات، ولا يقدم أي أسعار وهمية.",
            AppLanguage.FRENCH to "L'assistant IA se base exclusivement sur les prix vérifiés de la base de données.",
            AppLanguage.ENGLISH to "The AI assistant strictly uses verified database prices and never invents figures."
        ),
        "scan_barcode_hint" to mapOf(
            AppLanguage.ARABIC to "وجه الكاميرا نحو الباركود أو أدخل الرقم يدويًا",
            AppLanguage.FRENCH to "Scannez le code-barres ou saisissez-le manuellement",
            AppLanguage.ENGLISH to "Scan product barcode or enter code manually"
        ),
        "scan_btn" to mapOf(
            AppLanguage.ARABIC to "مسح الباركود",
            AppLanguage.FRENCH to "Scanner",
            AppLanguage.ENGLISH to "Scan"
        ),
        "product_not_found" to mapOf(
            AppLanguage.ARABIC to "المنتج غير موجود في القاعدة المحلية، يمكنك إضافته أو الاستعلام عبر API الخارجي.",
            AppLanguage.FRENCH to "Produit non trouvé en local. Vous pouvez le soumettre.",
            AppLanguage.ENGLISH to "Product not found locally. You can submit product details."
        )
    )

    fun get(key: String, language: AppLanguage): String {
        return translations[key]?.get(language) ?: translations[key]?.get(AppLanguage.ENGLISH) ?: key
    }
}
