package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.MainViewModel
import com.example.ui.theme.*

@Composable
fun PackagedFoodScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val packagedProducts by viewModel.packagedProducts.collectAsState()
    val allPrices by viewModel.allPrices.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("packaged_food_screen"),
        contentPadding = PaddingValues(bottom = 90.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📦", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.get("packaged", language),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MoroccanEmerald
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "السلع المصنعة والمعلبة من كبريات الشركات المغربية مع تتبع المكونات والقيم الغذائية وأكواد الباركود",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryDark
                )
            }
        }

        items(packagedProducts) { product ->
            val prodName = when (language) {
                AppLanguage.ARABIC -> product.nameAr
                AppLanguage.FRENCH -> product.nameFr
                AppLanguage.ENGLISH -> product.nameEn
            }

            val price = allPrices.find { it.productId == product.id && it.city == selectedCity }?.price

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openProductDetail(product.id) }
                    .testTag("packaged_card_${product.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(Color(0xFFF3ECE1), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (product.imageUrl.isNotEmpty()) {
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = prodName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text("📦", fontSize = 32.sp)
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            color = MoroccanGoldContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = product.brand.ifEmpty { "Marque Marocaine" },
                                color = Color(0xFF6D4C41),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = prodName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "المصنع: ${product.manufacturer} • الوزن: ${product.weight}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark,
                            fontSize = 10.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(12.dp), tint = TextSecondaryDark)
                            Text(
                                text = product.barcode,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondaryDark,
                                fontSize = 10.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        if (price != null) {
                            Text(
                                text = "${String.format("%.2f", price)} MAD / ${product.unit}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MoroccanEmerald
                            )
                        } else {
                            Text(
                                text = AppStrings.get("price_not_available", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF856404),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
