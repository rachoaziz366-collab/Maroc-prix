package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.PreloadedData
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitPriceScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()

    var selectedProduct by remember { mutableStateOf(allProducts.firstOrNull()) }
    var priceInput by remember { mutableStateOf("") }
    var cityInput by remember { mutableStateOf(selectedCity) }
    var storeNameInput by remember { mutableStateOf("") }
    var contributorEmail by remember { mutableStateOf("citoyen@morocco.ma") }
    var showProductDropdown by remember { mutableStateOf(false) }
    var showCityDropdown by remember { mutableStateOf(false) }
    var isSubmittedSuccess by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(AppStrings.get("submit_price", language), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .testTag("submit_price_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MoroccanEmeraldContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AddLocationAlt, contentDescription = null, tint = MoroccanOnEmeraldContainer)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = AppStrings.get("submit_notice", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = MoroccanOnEmeraldContainer,
                        lineHeight = 18.sp
                    )
                }
            }

            if (isSubmittedSuccess) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PriceDecreaseGreen.copy(0.15f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PriceDecreaseGreen, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "تم إرسال السعر بنجاح إلى جدول المراجعة والتدقيق!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PriceDecreaseGreen
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "سيقوم المشرفون بالتحقق منه ومطابقته قبل إضافته رسمياً لقاعدة بيانات الأسعار.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onBack() },
                            colors = ButtonDefaults.buttonColors(containerColor = MoroccanEmerald)
                        ) {
                            Text("العودة إلى الرئيسية")
                        }
                    }
                }
            } else {
                // Product Selection
                Box {
                    OutlinedCard(
                        onClick = { showProductDropdown = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("المنتج الغذائي", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedProduct?.nameAr ?: "اختر منتجاً...",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = showProductDropdown,
                        onDismissRequest = { showProductDropdown = false }
                    ) {
                        allProducts.forEach { p ->
                            DropdownMenuItem(
                                text = { Text("${p.nameAr} (${p.category})") },
                                onClick = {
                                    selectedProduct = p
                                    showProductDropdown = false
                                }
                            )
                        }
                    }
                }

                // Price Input
                OutlinedTextField(
                    value = priceInput,
                    onValueChange = { priceInput = it },
                    label = { Text("السعر الملاحظ بالمحل أو السوق (بالدرهم)") },
                    placeholder = { Text("مثال: 7.50") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // City Selection
                Box {
                    OutlinedCard(
                        onClick = { showCityDropdown = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("المدينة المغربية", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(cityInput, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                    DropdownMenu(
                        expanded = showCityDropdown,
                        onDismissRequest = { showCityDropdown = false }
                    ) {
                        PreloadedData.moroccanCities.forEach { c ->
                            DropdownMenuItem(
                                text = { Text("${c.nameAr} (${c.nameEn})") },
                                onClick = {
                                    cityInput = c.nameEn
                                    showCityDropdown = false
                                }
                            )
                        }
                    }
                }

                // Store or Market name
                OutlinedTextField(
                    value = storeNameInput,
                    onValueChange = { storeNameInput = it },
                    label = { Text("اسم المتجر أو السوق (اختياري)") },
                    placeholder = { Text("سوق الجملة، متجر كارفور، إپيسري الحي...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Contributor email
                OutlinedTextField(
                    value = contributorEmail,
                    onValueChange = { contributorEmail = it },
                    label = { Text("البريد الإلكتروني للمساهم") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val p = priceInput.toDoubleOrNull()
                        val prod = selectedProduct
                        if (p != null && prod != null && cityInput.isNotBlank()) {
                            viewModel.submitPrice(
                                productId = prod.id,
                                productName = prod.nameAr,
                                price = p,
                                city = cityInput,
                                storeName = storeNameInput.ifEmpty { "سوق محلي" },
                                submittedBy = contributorEmail
                            )
                            isSubmittedSuccess = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MoroccanEmerald),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_price_confirm_btn")
                ) {
                    Text("إرسال السعر للمراجعة والتوثيق", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}
