package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.components.ProductCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val productsWithPrices by viewModel.productsWithPrices.collectAsState()
    val favorites by viewModel.allFavorites.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val filteredProducts = if (selectedCategory != null) {
        productsWithPrices.filter { it.product.category == selectedCategory }
    } else {
        productsWithPrices
    }

    val priceIncreases = productsWithPrices.filter { (it.priceChangePercent ?: 0.0) > 0 }
    val priceDecreases = productsWithPrices.filter { (it.priceChangePercent ?: 0.0) < 0 }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_content"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(MoroccanEmeraldDark, MoroccanEmerald)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MoroccanGold,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "🇲🇦 " + AppStrings.get("verified_badge", language),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = selectedCity,
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = AppStrings.get("tagline", language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "أسعار موثقة من أسواق الجملة والوزارة دون أي أسعار وهمية",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick action shortcut pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.navigateTo(Screen.COMPARE) },
                            colors = ButtonDefaults.buttonColors(containerColor = MoroccanTerracotta),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_compare_shortcut")
                        ) {
                            Icon(Icons.Default.CompareArrows, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppStrings.get("compare", language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.navigateTo(Screen.AI_ASSISTANT) },
                            colors = ButtonDefaults.buttonColors(containerColor = MoroccanGold),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_ai_shortcut")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppStrings.get("ai_assistant", language), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }
        }

        // Quick feature navigators (Terroir & Packaged & Scanner)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Moroccan Local Products Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.TERROIR) }
                        .testTag("shortcut_terroir"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MoroccanEmeraldContainer)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("🏺", fontSize = 26.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = AppStrings.get("terroir", language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MoroccanOnEmeraldContainer
                        )
                        Text(
                            text = "أركان، زعفران، تمور...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MoroccanOnEmeraldContainer.copy(alpha = 0.8f),
                            fontSize = 10.sp
                        )
                    }
                }

                // Packaged Food Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.PACKAGED) }
                        .testTag("shortcut_packaged"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MoroccanTerracottaContainer)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("📦", fontSize = 26.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = AppStrings.get("packaged", language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MoroccanOnTerracottaContainer
                        )
                        Text(
                            text = "شاي، كسكس، معلبات...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MoroccanOnTerracottaContainer.copy(alpha = 0.8f),
                            fontSize = 10.sp
                        )
                    }
                }

                // User Price Submission Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(Screen.SUBMIT_PRICE) }
                        .testTag("shortcut_submit_price"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MoroccanGoldContainer)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("✍️", fontSize = 26.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = AppStrings.get("submit_price", language),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037)
                        )
                        Text(
                            text = "ساهم بسعر مدينتك",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF5D4037).copy(alpha = 0.8f),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // 19 Categories Horizontal Strip
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.get("categories", language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (selectedCategory != null) {
                        TextButton(onClick = { viewModel.setSelectedCategory(null) }) {
                            Text("عرض الكل", color = MoroccanEmerald, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // "All" chip
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { viewModel.setSelectedCategory(null) },
                        label = { Text("الكل / Tous") },
                        leadingIcon = { Text("🛒") }
                    )

                    PreloadedData.categories.forEach { cat ->
                        val isSelected = selectedCategory == cat.nameEn
                        val catLabel = when (language) {
                            AppLanguage.ARABIC -> cat.nameAr
                            AppLanguage.FRENCH -> cat.nameFr
                            AppLanguage.ENGLISH -> cat.nameEn
                        }
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                viewModel.setSelectedCategory(if (isSelected) null else cat.nameEn)
                            },
                            label = { Text(catLabel) },
                            leadingIcon = { Text(cat.icon) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MoroccanEmerald,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("cat_chip_${cat.nameEn}")
                        )
                    }
                }
            }
        }

        // Products with Price Decreases (Baisses de prix)
        if (priceDecreases.isNotEmpty() && selectedCategory == null) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(PriceDecreaseGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = PriceDecreaseGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.get("price_decreases", language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(priceDecreases) { item ->
                        ProductCard(
                            item = item,
                            language = language,
                            isFavorite = favorites.any { it.productId == item.product.id },
                            onFavoriteToggle = { viewModel.toggleFavorite(item.product.id) },
                            onClick = { viewModel.openProductDetail(item.product.id) },
                            modifier = Modifier.width(220.dp)
                        )
                    }
                }
            }
        }

        // Today's Price Updates / Main Products list
        item {
            Spacer(modifier = Modifier.height(22.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedCategory != null) selectedCategory!! else AppStrings.get("todays_updates", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${filteredProducts.size} منتجات",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryDark
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(filteredProducts) { item ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                ProductCard(
                    item = item,
                    language = language,
                    isFavorite = favorites.any { it.productId == item.product.id },
                    onFavoriteToggle = { viewModel.toggleFavorite(item.product.id) },
                    onClick = { viewModel.openProductDetail(item.product.id) }
                )
            }
        }
    }
}
