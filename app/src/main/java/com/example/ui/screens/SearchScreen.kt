package com.example.ui.screens

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
import com.example.ui.components.ProductCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val productsWithPrices by viewModel.productsWithPrices.collectAsState()
    val favorites by viewModel.allFavorites.collectAsState()

    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var onlyTerroir by remember { mutableStateOf(false) }
    var onlyOrganic by remember { mutableStateOf(false) }
    var maxPriceLimit by remember { mutableStateOf(500f) }

    val searchResults = remember(searchQuery, productsWithPrices, selectedCategoryFilter, onlyTerroir, onlyOrganic, maxPriceLimit) {
        val q = searchQuery.trim().lowercase()
        productsWithPrices.filter { item ->
            val p = item.product
            val matchesQuery = q.isEmpty() ||
                p.nameAr.lowercase().contains(q) ||
                p.nameFr.lowercase().contains(q) ||
                p.nameEn.lowercase().contains(q) ||
                p.barcode.contains(q) ||
                p.manufacturer.lowercase().contains(q) ||
                p.region.lowercase().contains(q) ||
                p.category.lowercase().contains(q)

            val matchesCat = selectedCategoryFilter == null || p.category == selectedCategoryFilter
            val matchesTerroir = !onlyTerroir || p.isLocalTerroir
            val matchesOrganic = !onlyOrganic || p.isOrganic
            val matchesPrice = item.currentPrice == null || item.currentPrice <= maxPriceLimit

            matchesQuery && matchesCat && matchesTerroir && matchesOrganic && matchesPrice
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("search_screen"),
        contentPadding = PaddingValues(bottom = 90.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text(AppStrings.get("search_hint", language), fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MoroccanEmerald) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MoroccanEmerald,
                    unfocusedBorderColor = BorderSoft
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_text_input")
            )
        }

        // Advanced filter chips (Terroir, Organic, Max price)
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = onlyTerroir,
                        onClick = { onlyTerroir = !onlyTerroir },
                        label = { Text("🇲🇦 منتجات مجالية فقط") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MoroccanTerracotta,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = onlyOrganic,
                        onClick = { onlyOrganic = !onlyOrganic },
                        label = { Text("🌱 بيو / عضوي") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PriceDecreaseGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "الحد الأقصى للسعر: ${maxPriceLimit.toInt()} درهم",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryDark
                    )
                    Slider(
                        value = maxPriceLimit,
                        onValueChange = { maxPriceLimit = it },
                        valueRange = 10f..500f,
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                        colors = SliderDefaults.colors(thumbColor = MoroccanEmerald, activeTrackColor = MoroccanEmerald)
                    )
                }
            }
        }

        // Result count header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "نتائج البحث (${searchResults.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (searchResults.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔍", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "لم يتم العثور على نتائج تطابق معايير البحث",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondaryDark
                        )
                    }
                }
            }
        }

        items(searchResults) { item ->
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
