package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun TerroirScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val terroirProducts by viewModel.terroirProducts.collectAsState()
    val allPrices by viewModel.allPrices.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()

    var selectedRegionFilter by remember { mutableStateOf<String?>(null) }

    val regions = remember(terroirProducts) {
        terroirProducts.mapNotNull { it.region.ifEmpty { null } }.distinct()
    }

    val filtered = if (selectedRegionFilter != null) {
        terroirProducts.filter { it.region == selectedRegionFilter }
    } else {
        terroirProducts
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("terroir_screen"),
        contentPadding = PaddingValues(bottom = 90.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏺", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.get("terroir", language),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MoroccanTerracotta
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "منتجات مجالية مغربية أصيلة ذات مؤشرات جغرافية محمية (AOP / IGP) من تعاونيات نسائية وفلاحية محلية",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryDark
                )
            }
        }

        // Region Filter Pills
        if (regions.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedRegionFilter == null,
                        onClick = { selectedRegionFilter = null },
                        label = { Text("جميع جهات المملكة") }
                    )
                    regions.forEach { reg ->
                        FilterChip(
                            selected = selectedRegionFilter == reg,
                            onClick = { selectedRegionFilter = if (selectedRegionFilter == reg) null else reg },
                            label = { Text(reg) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MoroccanTerracotta,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Terroir Product Cards
        items(filtered) { product ->
            val prodName = when (language) {
                AppLanguage.ARABIC -> product.nameAr
                AppLanguage.FRENCH -> product.nameFr
                AppLanguage.ENGLISH -> product.nameEn
            }

            val cityPrice = allPrices.find { it.productId == product.id && it.city == selectedCity }?.price

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openProductDetail(product.id) }
                    .testTag("terroir_card_${product.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(Color(0xFFF7F3EB))
                    ) {
                        if (product.imageUrl.isNotEmpty()) {
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = prodName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("🇲🇦", fontSize = 48.sp)
                            }
                        }

                        // Certification tag
                        Surface(
                            color = MoroccanTerracotta,
                            shape = RoundedCornerShape(bottomEnd = 8.dp),
                            modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = product.certification.ifEmpty { "AOP / IGP Maroc" },
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (product.isOrganic) {
                            Surface(
                                color = PriceDecreaseGreen,
                                shape = RoundedCornerShape(bottomStart = 8.dp),
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Eco, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("Bio / عضوي", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = MoroccanEmerald, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${product.region} • ${product.manufacturingLocation}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MoroccanEmerald,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = prodName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        if (product.manufacturer.isNotEmpty()) {
                            Text(
                                text = "التعاونية: ${product.manufacturer}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }

                        if (product.productionMethod.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "طريقة الإنتاج: ${product.productionMethod}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark,
                                fontSize = 11.sp,
                                maxLines = 2
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (cityPrice != null) {
                                Text(
                                    text = "${String.format("%.2f", cityPrice)} MAD / ${product.unit}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MoroccanEmerald
                                )
                            } else {
                                Text(
                                    text = AppStrings.get("price_not_available", language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF856404),
                                    fontSize = 11.sp
                                )
                            }

                            TextButton(onClick = { viewModel.openProductDetail(product.id) }) {
                                Text("تفاصيل المنشأ والأسعار", color = MoroccanTerracotta, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
