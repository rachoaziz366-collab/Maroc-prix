package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.Screen
import com.example.ui.components.PriceTrendCanvasChart
import com.example.ui.components.ProductCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val allPrices by viewModel.allPrices.collectAsState()
    val favorites by viewModel.allFavorites.collectAsState()
    val productsWithPrices by viewModel.productsWithPrices.collectAsState()

    val product = allProducts.find { it.id == productId }
    val productPrices = allPrices.filter { it.productId == productId }
    val cityPrice = productPrices.find { it.city == selectedCity }
    val isFavorite = favorites.any { it.productId == productId }

    val historyFlow = remember(productId, selectedCity) {
        viewModel.getPriceHistory(productId, selectedCity)
    }
    val priceHistory by historyFlow.collectAsState(initial = emptyList())

    // Price Alert Dialog state
    var showAlertDialog by remember { mutableStateOf(false) }
    var targetPriceInput by remember { mutableStateOf("") }
    var selectedCondition by remember { mutableStateOf("BELOW") }

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MoroccanEmerald)
        }
        return
    }

    val prodName = when (language) {
        AppLanguage.ARABIC -> product.nameAr
        AppLanguage.FRENCH -> product.nameFr
        AppLanguage.ENGLISH -> product.nameEn
    }
    val altName = when (language) {
        AppLanguage.ARABIC -> product.nameFr
        else -> product.nameAr
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = prodName,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite(product.id) }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) MoroccanTerracotta else Color.Gray
                        )
                    }
                    IconButton(onClick = { showAlertDialog = true }) {
                        Icon(
                            Icons.Outlined.NotificationsActive,
                            contentDescription = "Price Alert",
                            tint = MoroccanGold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .testTag("product_detail_scroll"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Image Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFEBE6DD))
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
                            Text("🇲🇦", fontSize = 60.sp)
                        }
                    }

                    if (product.isLocalTerroir) {
                        Surface(
                            color = MoroccanTerracotta,
                            shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 12.dp),
                            modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            Text(
                                text = "🇲🇦 " + (product.certification.ifEmpty { "Produit du Terroir" }),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Names & Category
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MoroccanEmeraldContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = product.category,
                                color = MoroccanOnEmeraldContainer,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = "Barcode: ${product.barcode}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = prodName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = altName,
                        style = MaterialTheme.typography.titleSmall,
                        color = TextSecondaryDark
                    )
                }
            }

            // Current Price Display Card - Critical: if price not available, state "Price data is not available yet"
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${AppStrings.get("current_price", language)} ($selectedCity)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondaryDark
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MoroccanEmeraldContainer
                            ) {
                                Text(
                                    text = "City: $selectedCity",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MoroccanOnEmeraldContainer,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (cityPrice?.price != null) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = String.format("%.2f", cityPrice.price),
                                    style = MaterialTheme.typography.displaySmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MoroccanEmerald
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${cityPrice.currency} / ${product.unit}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextSecondaryDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = MoroccanEmerald,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Source: ${cityPrice.sourceName} • ${cityPrice.date}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryDark
                                )
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFFF3CD),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = Color(0xFF856404)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = AppStrings.get("price_not_available", language),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF856404)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Price History Chart
            item {
                Text(
                    text = AppStrings.get("price_history", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                PriceTrendCanvasChart(
                    history = priceHistory,
                    currency = "MAD"
                )
            }

            // Cross-City Price Comparison Table
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Prices in Other Moroccan Cities",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        if (productPrices.isNotEmpty()) {
                            productPrices.forEach { pr ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = pr.city,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (pr.city == selectedCity) FontWeight.Bold else FontWeight.Normal,
                                        color = if (pr.city == selectedCity) MoroccanEmerald else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${pr.price} ${pr.currency}/${pr.unit}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MoroccanEmerald
                                    )
                                }
                                HorizontalDivider(color = BorderSoft)
                            }
                        } else {
                            Text(
                                text = "Price data for other cities is not available yet.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark
                            )
                        }
                    }
                }
            }

            // Product Details: Description, Ingredients, Nutrition, Manufacturer
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Product Specifications",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        val desc = when (language) {
                            AppLanguage.ARABIC -> product.descriptionAr
                            AppLanguage.FRENCH -> product.descriptionFr
                            AppLanguage.ENGLISH -> product.descriptionEn
                        }
                        if (desc.isNotEmpty()) {
                            DetailItem(title = "Description", value = desc)
                        }

                        if (product.region.isNotEmpty()) {
                            DetailItem(title = "Region / Origin", value = "${product.region}, ${product.countryOfOrigin}")
                        }
                        if (product.manufacturer.isNotEmpty()) {
                            DetailItem(title = "Manufacturer / Cooperative", value = "${product.manufacturer} (${product.manufacturingLocation})")
                        }
                        if (product.ingredients.isNotEmpty()) {
                            DetailItem(title = "Ingredients", value = product.ingredients)
                        }
                        if (product.nutritionFacts.isNotEmpty()) {
                            DetailItem(title = "Nutrition Facts", value = product.nutritionFacts)
                        }
                        if (product.allergens.isNotEmpty()) {
                            DetailItem(title = "Allergens", value = product.allergens)
                        }
                        if (product.productBenefits.isNotEmpty()) {
                            DetailItem(title = "Benefits", value = product.productBenefits)
                        }
                    }
                }
            }

            // Quick actions: Submit price update & Alert button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showAlertDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MoroccanGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("detail_set_alert_btn")
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AppStrings.get("price_alerts", language), color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.navigateTo(Screen.SUBMIT_PRICE) },
                        colors = ButtonDefaults.buttonColors(containerColor = MoroccanTerracotta),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("detail_submit_price_btn")
                    ) {
                        Icon(Icons.Default.AddLocationAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AppStrings.get("submit_price", language), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Create Price Alert Dialog
    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = { showAlertDialog = false },
            title = { Text(AppStrings.get("create_alert", language)) },
            text = {
                Column {
                    Text(
                        text = "${prodName} ($selectedCity)",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = targetPriceInput,
                        onValueChange = { targetPriceInput = it },
                        label = { Text("Target Price (MAD)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Condition: Notify when price falls below target",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val p = targetPriceInput.toDoubleOrNull() ?: 0.0
                        if (p > 0) {
                            viewModel.createPriceAlert(
                                productId = product.id,
                                productName = prodName,
                                targetPrice = p,
                                city = selectedCity,
                                condition = selectedCondition
                            )
                        }
                        showAlertDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MoroccanEmerald)
                ) {
                    Text("Save Alert")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAlertDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DetailItem(title: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MoroccanEmerald,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
