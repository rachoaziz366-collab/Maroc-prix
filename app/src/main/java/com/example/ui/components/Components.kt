package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.PriceHistoryEntity
import com.example.data.model.ProductEntity
import com.example.data.model.ProductWithLatestPrice
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.theme.*

@Composable
fun MoroccanTopBar(
    title: String,
    selectedCity: String,
    currentLanguage: AppLanguage,
    onCityClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onSearchClick: () -> Unit,
    onScannerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MoroccanTerracotta),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🇲🇦", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = AppStrings.get("tagline", currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Barcode quick icon
                    IconButton(
                        onClick = onScannerClick,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("top_scanner_button")
                    ) {
                        Icon(
                            Icons.Outlined.QrCodeScanner,
                            contentDescription = "Barcode Scanner",
                            tint = Color.White
                        )
                    }

                    // Language switch badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier
                            .clickable(onClick = onLanguageClick)
                            .testTag("language_toggle_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Language,
                                contentDescription = "Language",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentLanguage.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // City Selection Bar & Search trigger
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // City selector chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onCityClick)
                        .testTag("city_selector_chip")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "City",
                            tint = MoroccanTerracotta,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = AppStrings.get("select_city", currentLanguage),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondaryDark,
                                fontSize = 9.sp
                            )
                            Text(
                                text = selectedCity,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryDark,
                                maxLines = 1
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = TextSecondaryDark
                        )
                    }
                }

                // Search trigger button
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MoroccanGoldLight,
                    modifier = Modifier
                        .clickable(onClick = onSearchClick)
                        .testTag("top_search_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.get("search_hint", currentLanguage).take(6) + "...",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    item: ProductWithLatestPrice,
    language: AppLanguage,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val productName = when (language) {
        AppLanguage.ARABIC -> item.product.nameAr
        AppLanguage.FRENCH -> item.product.nameFr
        AppLanguage.ENGLISH -> item.product.nameEn
    }
    val subtitleName = when (language) {
        AppLanguage.ARABIC -> item.product.nameFr
        else -> item.product.nameAr
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("product_card_${item.product.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Color(0xFFF0EBE1))
            ) {
                if (item.product.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = item.product.imageUrl,
                        contentDescription = productName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (item.product.isLocalTerroir) "🏺" else "🥦",
                            fontSize = 42.sp
                        )
                    }
                }

                // Terroir or Packaged tag
                if (item.product.isLocalTerroir) {
                    Surface(
                        color = MoroccanTerracotta,
                        shape = RoundedCornerShape(bottomEnd = 8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "🇲🇦 " + (item.product.region.ifEmpty { "Terroir Maroc" }),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp
                        )
                    }
                } else if (item.product.isPackaged) {
                    Surface(
                        color = MoroccanGold,
                        shape = RoundedCornerShape(bottomEnd = 8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = item.product.brand.ifEmpty { "Packaged" },
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp
                        )
                    }
                }

                // Favorite button
                IconButton(
                    onClick = onFavoriteToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.85f))
                        .testTag("fav_btn_${item.product.id}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) MoroccanTerracotta else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Info section
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.product.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MoroccanEmerald,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                    Text(
                        text = item.city,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryDark,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = productName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = subtitleName,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryDark,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price Section - Critical constraint: if null show "Price data is not available yet"
                if (item.currentPrice != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = String.format("%.2f", item.currentPrice),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MoroccanEmerald
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${item.currency}/${item.unit}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TextSecondaryDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Price change percent indicator
                        if (item.priceChangePercent != null) {
                            val isDecrease = item.priceChangePercent < 0
                            val isIncrease = item.priceChangePercent > 0
                            val badgeColor = when {
                                isDecrease -> PriceDecreaseGreen
                                isIncrease -> PriceIncreaseRed
                                else -> PriceStableGrey
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = badgeColor.copy(alpha = 0.12f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = when {
                                            isDecrease -> Icons.Default.TrendingDown
                                            isIncrease -> Icons.Default.TrendingUp
                                            else -> Icons.Default.TrendingFlat
                                        },
                                        contentDescription = null,
                                        tint = badgeColor,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${if (isIncrease) "+" else ""}${String.format("%.1f", item.priceChangePercent)}%",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = badgeColor,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Verification Source Tag
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = MoroccanEmerald,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = item.sourceName.ifEmpty { AppStrings.get("verified_badge", language) },
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondaryDark,
                            fontSize = 9.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    // No price connected yet
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFFFF3CD),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFF856404),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
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

@Composable
fun PriceTrendCanvasChart(
    history: List<PriceHistoryEntity>,
    currency: String = "MAD",
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFFF9F7F2), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Historical tracking begins as market records accumulate",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        return
    }

    val prices = history.map { it.price }
    val minPrice = (prices.minOrNull() ?: 0.0) * 0.9
    val maxPrice = (prices.maxOrNull() ?: 10.0) * 1.1
    val range = if (maxPrice > minPrice) maxPrice - minPrice else 1.0

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(1.dp, BorderSoft, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Interactive Price Evolution",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            val latest = prices.lastOrNull() ?: 0.0
            val first = prices.firstOrNull() ?: 0.0
            val diff = if (first > 0) ((latest - first) / first) * 100 else 0.0
            val isUp = diff > 0
            val isDown = diff < 0
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (isDown) PriceDecreaseGreen.copy(0.1f) else if (isUp) PriceIncreaseRed.copy(0.1f) else Color.LightGray.copy(0.2f)
            ) {
                Text(
                    text = "${if (isUp) "+" else ""}${String.format("%.1f", diff)}% overall",
                    color = if (isDown) PriceDecreaseGreen else if (isUp) PriceIncreaseRed else Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            val width = size.width
            val height = size.height
            val stepX = if (history.size > 1) width / (history.size - 1) else width

            // Draw grid lines
            drawLine(
                color = Color.LightGray.copy(alpha = 0.4f),
                start = Offset(0f, 0f),
                end = Offset(width, 0f),
                strokeWidth = 1f
            )
            drawLine(
                color = Color.LightGray.copy(alpha = 0.4f),
                start = Offset(0f, height / 2),
                end = Offset(width, height / 2),
                strokeWidth = 1f
            )
            drawLine(
                color = Color.LightGray.copy(alpha = 0.4f),
                start = Offset(0f, height),
                end = Offset(width, height),
                strokeWidth = 1f
            )

            // Construct line path & gradient area
            val path = Path()
            val fillPath = Path()
            val points = mutableListOf<Offset>()

            history.forEachIndexed { index, record ->
                val x = index * stepX
                val yNorm = (record.price - minPrice) / range
                val y = height - (yNorm * height).toFloat()
                points.add(Offset(x, y))
                if (index == 0) {
                    path.moveTo(x, y)
                    fillPath.moveTo(x, height)
                    fillPath.lineTo(x, y)
                } else {
                    path.lineTo(x, y)
                    fillPath.lineTo(x, y)
                }
            }

            fillPath.lineTo(points.last().x, height)
            fillPath.close()

            // Draw gradient under curve
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MoroccanEmerald.copy(alpha = 0.25f),
                        MoroccanEmerald.copy(alpha = 0.02f)
                    )
                )
            )

            // Draw stroke line
            drawPath(
                path = path,
                color = MoroccanEmerald,
                style = Stroke(width = 3.dp.toPx())
            )

            // Draw points
            points.forEach { pt ->
                drawCircle(color = Color.White, radius = 5.dp.toPx(), center = pt)
                drawCircle(color = MoroccanEmerald, radius = 3.dp.toPx(), center = pt)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // X-Axis Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            history.forEach { record ->
                Text(
                    text = record.periodLabel.replace(" Ago", ""),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = TextSecondaryDark,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
