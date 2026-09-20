package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.MainViewModel
import com.example.ui.components.ProductCard
import com.example.ui.theme.*

@Composable
fun FavoritesAndAlertsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val favorites by viewModel.allFavorites.collectAsState()
    val alerts by viewModel.allAlerts.collectAsState()
    val productsWithPrices by viewModel.productsWithPrices.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Favorites, 1: Alerts

    val favoriteProducts = remember(favorites, productsWithPrices) {
        val favIds = favorites.map { it.productId }.toSet()
        productsWithPrices.filter { favIds.contains(it.product.id) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("favorites_alerts_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Tab Selector
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = MoroccanEmerald
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        "${AppStrings.get("favorites", language)} (${favoriteProducts.size})",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        "${AppStrings.get("price_alerts", language)} (${alerts.size})",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 0) {
            if (favoriteProducts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("❤️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "لا توجد منتجات محفوظة في المفضلة بعد",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondaryDark
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoriteProducts) { item ->
                        ProductCard(
                            item = item,
                            language = language,
                            isFavorite = true,
                            onFavoriteToggle = { viewModel.toggleFavorite(item.product.id) },
                            onClick = { viewModel.openProductDetail(item.product.id) }
                        )
                    }
                }
            }
        } else {
            // Price Alerts list
            if (alerts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔔", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "لا توجد تنبيهات أسعار نشطة حاليًا",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondaryDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "يمكنك تفعيل تنبيه من صفحة أي منتج عند انخفاض سعره",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(alerts) { alert ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = MoroccanGold
                                    )
                                    Column {
                                        Text(
                                            text = alert.productName,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "الهدف: ${alert.targetPrice} درهم في ${alert.city}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MoroccanEmerald,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "الشرط: عند انخفاض السعر عن الحد المحدد",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondaryDark,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                IconButton(onClick = { viewModel.deletePriceAlert(alert.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
