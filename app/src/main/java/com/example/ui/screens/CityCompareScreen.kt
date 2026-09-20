package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityCompareScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val compareCity1 by viewModel.compareCity1.collectAsState()
    val compareCity2 by viewModel.compareCity2.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val allPrices by viewModel.allPrices.collectAsState()

    var showCity1Dropdown by remember { mutableStateOf(false) }
    var showCity2Dropdown by remember { mutableStateOf(false) }

    val comparisonList = remember(allProducts, allPrices, compareCity1, compareCity2) {
        allProducts.mapNotNull { prod ->
            val p1 = allPrices.find { it.productId == prod.id && it.city == compareCity1 }?.price
            val p2 = allPrices.find { it.productId == prod.id && it.city == compareCity2 }?.price

            if (p1 != null || p2 != null) {
                val diff = if (p1 != null && p2 != null) p2 - p1 else null
                val pct = if (p1 != null && p2 != null && p1 > 0) ((p2 - p1) / p1) * 100 else null
                CityComparisonItem(
                    product = prod,
                    priceCity1 = p1,
                    priceCity2 = p2,
                    difference = diff,
                    percentChange = pct,
                    unit = prod.unit
                )
            } else null
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("city_compare_screen"),
        contentPadding = PaddingValues(bottom = 90.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = AppStrings.get("compare", language),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MoroccanEmerald
            )
            Text(
                text = "قارن بين أسعار السلع والمنتجات بين مدينتين مغربيتين لمعرفة فروقات الأسعار المعتمدة",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryDark
            )
        }

        // City Selector Cards
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // City 1
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedCard(
                                onClick = { showCity1Dropdown = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("المدينة 1 (Base)", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(compareCity1, fontWeight = FontWeight.Bold, color = MoroccanEmerald, style = MaterialTheme.typography.titleSmall)
                                }
                            }
                            DropdownMenu(
                                expanded = showCity1Dropdown,
                                onDismissRequest = { showCity1Dropdown = false }
                            ) {
                                PreloadedData.moroccanCities.forEach { city ->
                                    DropdownMenuItem(
                                        text = { Text(city.nameEn + " (" + city.nameAr + ")") },
                                        onClick = {
                                            viewModel.setCompareCity1(city.nameEn)
                                            showCity1Dropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        Icon(Icons.Default.CompareArrows, contentDescription = null, tint = MoroccanTerracotta)

                        // City 2
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedCard(
                                onClick = { showCity2Dropdown = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("المدينة 2 (Target)", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(compareCity2, fontWeight = FontWeight.Bold, color = MoroccanTerracotta, style = MaterialTheme.typography.titleSmall)
                                }
                            }
                            DropdownMenu(
                                expanded = showCity2Dropdown,
                                onDismissRequest = { showCity2Dropdown = false }
                            ) {
                                PreloadedData.moroccanCities.forEach { city ->
                                    DropdownMenuItem(
                                        text = { Text(city.nameEn + " (" + city.nameAr + ")") },
                                        onClick = {
                                            viewModel.setCompareCity2(city.nameEn)
                                            showCity2Dropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Summary bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "نتائج المقارنة (${comparisonList.size} منتجات متطابقة)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Comparison Items
        items(comparisonList) { item ->
            val prodName = when (language) {
                AppLanguage.ARABIC -> item.product.nameAr
                AppLanguage.FRENCH -> item.product.nameFr
                AppLanguage.ENGLISH -> item.product.nameEn
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openProductDetail(item.product.id) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = prodName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = item.product.category + " • " + item.unit,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark,
                                fontSize = 11.sp
                            )
                        }

                        // Percentage change pill
                        if (item.percentChange != null) {
                            val isMoreExpensive = item.percentChange > 0
                            val isCheaper = item.percentChange < 0
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isMoreExpensive) PriceIncreaseRed.copy(0.12f) else if (isCheaper) PriceDecreaseGreen.copy(0.12f) else Color.LightGray.copy(0.2f)
                            ) {
                                Text(
                                    text = "${if (isMoreExpensive) "+" else ""}${String.format("%.1f", item.percentChange)}%",
                                    color = if (isMoreExpensive) PriceIncreaseRed else if (isCheaper) PriceDecreaseGreen else Color.DarkGray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2-Column Price Comparison
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // City 1 Box
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = MoroccanEmerald.copy(alpha = 0.08f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(compareCity1, style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (item.priceCity1 != null) "${String.format("%.2f", item.priceCity1)} MAD" else "N/A",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MoroccanEmerald
                                )
                            }
                        }

                        // City 2 Box
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = MoroccanTerracotta.copy(alpha = 0.08f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(compareCity2, style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (item.priceCity2 != null) "${String.format("%.2f", item.priceCity2)} MAD" else "N/A",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MoroccanTerracotta
                                )
                            }
                        }
                    }

                    // Explanation Note
                    if (item.difference != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val isCheaper = item.difference < 0
                        val winnerCity = if (isCheaper) compareCity2 else compareCity1
                        val diffAbs = kotlin.math.abs(item.difference)
                        Text(
                            text = "💡 أرخص في $winnerCity بفارق ${String.format("%.2f", diffAbs)} درهم",
                            style = MaterialTheme.typography.labelSmall,
                            color = MoroccanEmerald,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

data class CityComparisonItem(
    val product: com.example.data.model.ProductEntity,
    val priceCity1: Double?,
    val priceCity2: Double?,
    val difference: Double?,
    val percentChange: Double?,
    val unit: String
)
