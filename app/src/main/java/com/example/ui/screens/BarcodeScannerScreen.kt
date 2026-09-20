package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.*

@Composable
fun BarcodeScannerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val scannedBarcode by viewModel.scannedBarcode.collectAsState()
    val scannedProduct by viewModel.scannedProduct.collectAsState()
    val allPrices by viewModel.allPrices.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()

    var manualCodeInput by remember { mutableStateOf("") }
    var isSimulatingScan by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("barcode_scanner_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.QrCodeScanner, contentDescription = null, tint = MoroccanEmerald, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = AppStrings.get("scanner", language),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MoroccanEmerald
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = AppStrings.get("scan_barcode_hint", language),
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondaryDark,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Simulated Scanner Viewport with Moroccan frame styling
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF15221B))
                .border(2.dp, MoroccanEmerald, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.CropFree,
                    contentDescription = null,
                    tint = MoroccanGoldLight,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isSimulatingScan) "جاري قراءة الرمز الشريطي..." else "مستشعر كاميرا الباركود جاهز",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick sample test barcodes (Moroccan National 611 prefix)
        Text(
            text = "رموز شريطية مغربية تجريبية سريعة (بادئة 611 بالمغرب):",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondaryDark,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = {
                    manualCodeInput = "6111001230014"
                    viewModel.scanBarcode("6111001230014")
                },
                label = { Text("شاي سلطان") }
            )
            AssistChip(
                onClick = {
                    manualCodeInput = "6111032100451"
                    viewModel.scanBarcode("6111032100451")
                },
                label = { Text("كسكس داري") }
            )
            AssistChip(
                onClick = {
                    manualCodeInput = "6111234567892"
                    viewModel.scanBarcode("6111234567892")
                },
                label = { Text("زيت أركان AOP") }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Manual Barcode Input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = manualCodeInput,
                onValueChange = { manualCodeInput = it },
                label = { Text("أدخل رقم الباركود (EAN-13)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            Button(
                onClick = {
                    if (manualCodeInput.isNotBlank()) {
                        viewModel.scanBarcode(manualCodeInput.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MoroccanEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Text(AppStrings.get("scan_btn", language), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Result Card
        if (scannedProduct != null) {
            val prod = scannedProduct!!
            val price = allPrices.find { it.productId == prod.id && it.city == selectedCity }?.price
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(color = MoroccanEmeraldContainer, shape = RoundedCornerShape(6.dp)) {
                            Text(
                                text = "تم التعرف على المنتج ✅",
                                color = MoroccanOnEmeraldContainer,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(prod.barcode, style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(prod.nameAr, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(prod.nameFr, style = MaterialTheme.typography.bodySmall, color = TextSecondaryDark)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("المصنع: ${prod.manufacturer} • المنشأ: ${prod.countryOfOrigin}", style = MaterialTheme.typography.bodySmall)

                    if (price != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "السعر المعتمد في $selectedCity: ${String.format("%.2f", price)} درهم / ${prod.unit}",
                            style = MaterialTheme.typography.titleSmall,
                            color = MoroccanEmerald,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.openProductDetail(prod.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = MoroccanEmerald),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("عرض التفاصيل كاملة")
                        }
                        OutlinedButton(
                            onClick = { viewModel.navigateTo(Screen.SUBMIT_PRICE) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("الإبلاغ عن سعر")
                        }
                    }
                }
            }
        } else if (scannedBarcode.isNotEmpty()) {
            // Not found in local database
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6)),
                border = CardDefaults.outlinedCardBorder(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "الرمز $scannedBarcode غير مسجل في السجل المحلي بعد",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF856404)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = AppStrings.get("product_not_found", language),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF856404)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { viewModel.navigateTo(Screen.ADMIN) },
                        colors = ButtonDefaults.buttonColors(containerColor = MoroccanTerracotta),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("إضافة هذا المنتج عبر لوحة التحكم")
                    }
                }
            }
        }
    }
}
