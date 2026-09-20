package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.db.FoodPriceDatabase
import com.example.data.repository.FoodPriceRepository
import com.example.data.repository.PreloadedData
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.MoroccanTopBar
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = FoodPriceDatabase.getDatabase(this)
        val repository = FoodPriceRepository(database.foodPriceDao())

        val viewModel by viewModels<MainViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return MainViewModel(repository) as T
                    }
                }
            }
        )

        setContent {
            val language by viewModel.currentLanguage.collectAsState()
            val selectedCity by viewModel.selectedCity.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()
            val selectedProductId by viewModel.selectedProductId.collectAsState()

            var showCityDialog by remember { mutableStateOf(false) }
            var showLanguageDialog by remember { mutableStateOf(false) }

            CompositionLocalProvider(LocalLayoutDirection provides language.layoutDirection) {
                MoroccoFoodPricesTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            if (currentScreen != Screen.PRODUCT_DETAIL && currentScreen != Screen.SUBMIT_PRICE) {
                                MoroccanTopBar(
                                    title = AppStrings.get("app_name", language),
                                    selectedCity = selectedCity,
                                    currentLanguage = language,
                                    onCityClick = { showCityDialog = true },
                                    onLanguageClick = { showLanguageDialog = true },
                                    onSearchClick = { viewModel.navigateTo(Screen.SEARCH) },
                                    onScannerClick = { viewModel.navigateTo(Screen.SCANNER) }
                                )
                            }
                        },
                        bottomBar = {
                            if (currentScreen != Screen.PRODUCT_DETAIL && currentScreen != Screen.SUBMIT_PRICE) {
                                NavigationBar(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 8.dp,
                                    modifier = Modifier.testTag("main_bottom_nav")
                                ) {
                                    NavigationBarItem(
                                        selected = currentScreen == Screen.HOME,
                                        onClick = { viewModel.navigateTo(Screen.HOME) },
                                        icon = {
                                            Icon(
                                                if (currentScreen == Screen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                                contentDescription = AppStrings.get("home", language)
                                            )
                                        },
                                        label = { Text(AppStrings.get("home", language), fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MoroccanEmerald,
                                            selectedTextColor = MoroccanEmerald,
                                            indicatorColor = MoroccanEmeraldContainer
                                        )
                                    )

                                    NavigationBarItem(
                                        selected = currentScreen == Screen.TERROIR,
                                        onClick = { viewModel.navigateTo(Screen.TERROIR) },
                                        icon = {
                                            Icon(
                                                if (currentScreen == Screen.TERROIR) Icons.Filled.Storefront else Icons.Outlined.Storefront,
                                                contentDescription = AppStrings.get("terroir", language)
                                            )
                                        },
                                        label = { Text(AppStrings.get("terroir", language), fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MoroccanTerracotta,
                                            selectedTextColor = MoroccanTerracotta,
                                            indicatorColor = MoroccanTerracottaContainer
                                        )
                                    )

                                    NavigationBarItem(
                                        selected = currentScreen == Screen.COMPARE,
                                        onClick = { viewModel.navigateTo(Screen.COMPARE) },
                                        icon = {
                                            Icon(
                                                Icons.Default.CompareArrows,
                                                contentDescription = AppStrings.get("compare", language)
                                            )
                                        },
                                        label = { Text(AppStrings.get("compare", language), fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MoroccanEmerald,
                                            selectedTextColor = MoroccanEmerald,
                                            indicatorColor = MoroccanEmeraldContainer
                                        )
                                    )

                                    NavigationBarItem(
                                        selected = currentScreen == Screen.FAVORITES,
                                        onClick = { viewModel.navigateTo(Screen.FAVORITES) },
                                        icon = {
                                            Icon(
                                                if (currentScreen == Screen.FAVORITES) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                                contentDescription = AppStrings.get("favorites", language)
                                            )
                                        },
                                        label = { Text(AppStrings.get("favorites", language), fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MoroccanTerracotta,
                                            selectedTextColor = MoroccanTerracotta,
                                            indicatorColor = MoroccanTerracottaContainer
                                        )
                                    )

                                    NavigationBarItem(
                                        selected = currentScreen == Screen.AI_ASSISTANT,
                                        onClick = { viewModel.navigateTo(Screen.AI_ASSISTANT) },
                                        icon = {
                                            Icon(
                                                if (currentScreen == Screen.AI_ASSISTANT) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                                                contentDescription = AppStrings.get("ai_assistant", language)
                                            )
                                        },
                                        label = { Text(AppStrings.get("ai_assistant", language), fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MoroccanGold,
                                            selectedTextColor = MoroccanGold,
                                            indicatorColor = MoroccanGoldContainer
                                        )
                                    )

                                    NavigationBarItem(
                                        selected = currentScreen == Screen.SETTINGS,
                                        onClick = { viewModel.navigateTo(Screen.SETTINGS) },
                                        icon = {
                                            Icon(
                                                if (currentScreen == Screen.SETTINGS) Icons.Filled.Settings else Icons.Outlined.Settings,
                                                contentDescription = AppStrings.get("settings", language)
                                            )
                                        },
                                        label = { Text(AppStrings.get("settings", language), fontSize = 11.sp) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MoroccanEmerald,
                                            selectedTextColor = MoroccanEmerald,
                                            indicatorColor = MoroccanEmeraldContainer
                                        )
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            when (currentScreen) {
                                Screen.HOME -> HomeScreen(viewModel = viewModel)
                                Screen.TERROIR -> TerroirScreen(viewModel = viewModel)
                                Screen.PACKAGED -> PackagedFoodScreen(viewModel = viewModel)
                                Screen.COMPARE -> CityCompareScreen(viewModel = viewModel)
                                Screen.FAVORITES -> FavoritesAndAlertsScreen(viewModel = viewModel)
                                Screen.SCANNER -> BarcodeScannerScreen(viewModel = viewModel)
                                Screen.AI_ASSISTANT -> AiAssistantScreen(viewModel = viewModel)
                                Screen.ADMIN -> AdminDashboardScreen(viewModel = viewModel)
                                Screen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                                Screen.SEARCH -> SearchScreen(viewModel = viewModel)
                                Screen.PRODUCT_DETAIL -> {
                                    if (selectedProductId != null) {
                                        ProductDetailScreen(
                                            productId = selectedProductId!!,
                                            viewModel = viewModel,
                                            onBack = { viewModel.navigateTo(Screen.HOME) }
                                        )
                                    }
                                }
                                Screen.SUBMIT_PRICE -> {
                                    SubmitPriceScreen(
                                        viewModel = viewModel,
                                        onBack = { viewModel.navigateTo(Screen.HOME) }
                                    )
                                }
                            }
                        }
                    }

                    // City Selection Dialog
                    if (showCityDialog) {
                        AlertDialog(
                            onDismissRequest = { showCityDialog = false },
                            title = {
                                Text(
                                    AppStrings.get("select_city", language),
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            text = {
                                LazyColumn(modifier = Modifier.height(320.dp)) {
                                    items(PreloadedData.moroccanCities) { c ->
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (c.nameEn == selectedCity) MoroccanEmeraldContainer else Color.Transparent,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    viewModel.setSelectedCity(c.nameEn)
                                                    showCityDialog = false
                                                }
                                                .padding(vertical = 4.dp)
                                                .testTag("dialog_city_${c.nameEn}")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(
                                                        text = "${c.nameAr} (${c.nameEn})",
                                                        fontWeight = if (c.nameEn == selectedCity) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (c.nameEn == selectedCity) MoroccanOnEmeraldContainer else MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = c.region,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = TextSecondaryDark,
                                                        fontSize = 10.sp
                                                    )
                                                }
                                                if (c.nameEn == selectedCity) {
                                                    Icon(Icons.Default.Check, contentDescription = null, tint = MoroccanEmerald)
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = { showCityDialog = false }) {
                                    Text("إغلاق")
                                }
                            }
                        )
                    }

                    // Language Selection Dialog
                    if (showLanguageDialog) {
                        AlertDialog(
                            onDismissRequest = { showLanguageDialog = false },
                            title = { Text(AppStrings.get("language", language), fontWeight = FontWeight.Bold) },
                            text = {
                                Column {
                                    AppLanguage.values().forEach { l ->
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (l == language) MoroccanEmeraldContainer else Color.Transparent,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    viewModel.setLanguage(l)
                                                    showLanguageDialog = false
                                                }
                                                .padding(vertical = 4.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(14.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = when (l) {
                                                        AppLanguage.ARABIC -> "🇲🇦 العربية (Arabic - RTL)"
                                                        AppLanguage.FRENCH -> "🇫🇷 Français (French)"
                                                        AppLanguage.ENGLISH -> "🇬🇧 English"
                                                    },
                                                    fontWeight = if (l == language) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (l == language) MoroccanOnEmeraldContainer else MaterialTheme.colorScheme.onSurface
                                                )
                                                if (l == language) {
                                                    Icon(Icons.Default.Check, contentDescription = null, tint = MoroccanEmerald)
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = { showLanguageDialog = false }) {
                                    Text("إغلاق")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
