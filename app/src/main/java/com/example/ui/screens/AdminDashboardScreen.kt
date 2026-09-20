package com.example.ui.screens

import androidx.compose.foundation.background
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
import com.example.data.model.PriceRecordEntity
import com.example.data.model.ProductEntity
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.MainViewModel
import com.example.ui.UserRole
import com.example.ui.theme.*
import java.util.UUID

@Composable
fun AdminDashboardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val allPrices by viewModel.allPrices.collectAsState()
    val allSources by viewModel.allSources.collectAsState()
    val allSubmissions by viewModel.allSubmissions.collectAsState()
    val auditLogs by viewModel.auditLogs.collectAsState()

    var activeAdminTab by remember { mutableStateOf(0) } // 0: Overview & Moderation, 1: Products, 2: Sources, 3: Audit Logs

    // Dialog for adding a new product
    var showAddProductDialog by remember { mutableStateOf(false) }
    var newProdNameAr by remember { mutableStateOf("") }
    var newProdNameFr by remember { mutableStateOf("") }
    var newProdNameEn by remember { mutableStateOf("") }
    var newProdCategory by remember { mutableStateOf("Vegetables") }
    var newProdBarcode by remember { mutableStateOf("611" + (1000000000L..9999999999L).random()) }

    // Dialog for setting a price
    var showAddPriceDialog by remember { mutableStateOf(false) }
    var priceInput by remember { mutableStateOf("") }
    var priceCityInput by remember { mutableStateOf("Casablanca") }
    var selectedProdForPrice by remember { mutableStateOf<ProductEntity?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("admin_dashboard_screen"),
        contentPadding = PaddingValues(bottom = 90.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.get("admin", language),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MoroccanEmerald
                    )
                    Text(
                        text = "نظام الحوكمة وإدارة الأسعار المعتمدة",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MoroccanTerracottaContainer
                ) {
                    Text(
                        text = currentRole.name,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MoroccanOnTerracottaContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Stats Overview Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "المنتجات المسجلة",
                    value = allProducts.size.toString(),
                    icon = "🥕",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "سجلات الأسعار",
                    value = allPrices.size.toString(),
                    icon = "📊",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "مساهمات معلقة",
                    value = allSubmissions.count { it.status == "PENDING" }.toString(),
                    icon = "⏳",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Admin Sub-Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = activeAdminTab,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                contentColor = MoroccanEmerald
            ) {
                Tab(
                    selected = activeAdminTab == 0,
                    onClick = { activeAdminTab = 0 },
                    text = { Text("المراجعة (${allSubmissions.count { it.status == "PENDING" }})") }
                )
                Tab(
                    selected = activeAdminTab == 1,
                    onClick = { activeAdminTab = 1 },
                    text = { Text("إدارة المنتجات") }
                )
                Tab(
                    selected = activeAdminTab == 2,
                    onClick = { activeAdminTab = 2 },
                    text = { Text("المصادر وAPIs") }
                )
                Tab(
                    selected = activeAdminTab == 3,
                    onClick = { activeAdminTab = 3 },
                    text = { Text("سجل الأمان (Audit)") }
                )
            }
        }

        // Tab 0: Moderation Queue
        if (activeAdminTab == 0) {
            val pendingSubs = allSubmissions.filter { it.status == "PENDING" }
            if (pendingSubs.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
                        Text("لا توجد مساهمات قيد الانتظار حالياً 🚀", color = TextSecondaryDark)
                    }
                }
            } else {
                items(pendingSubs) { sub ->
                    Card(
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
                                Text(sub.productName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFFF3CD)) {
                                    Text("قيد المراجعة", color = Color(0xFF856404), style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("السعر المقترح: ${sub.price} MAD في مدينة ${sub.city} (${sub.storeName})", style = MaterialTheme.typography.bodyMedium, color = MoroccanEmerald, fontWeight = FontWeight.Bold)
                            Text("المُرسل: ${sub.submittedBy} • التاريخ: ${sub.submissionDate}", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.moderateSubmission(sub.id, "APPROVED") },
                                    colors = ButtonDefaults.buttonColors(containerColor = PriceDecreaseGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(AppStrings.get("approve", language))
                                }
                                OutlinedButton(
                                    onClick = { viewModel.moderateSubmission(sub.id, "REJECTED", "Price out of bounds") },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp), tint = PriceIncreaseRed)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(AppStrings.get("reject", language), color = PriceIncreaseRed)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Tab 1: Manage Products
        if (activeAdminTab == 1) {
            item {
                Button(
                    onClick = { showAddProductDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MoroccanEmerald),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("add_product_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("إضافة منتج جديد لقاعدة البيانات")
                }
            }

            items(allProducts) { p ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(p.nameAr, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            Text(p.nameFr + " • " + p.category, style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                            Text("كود: " + p.barcode, style = MaterialTheme.typography.labelSmall, color = MoroccanEmerald)
                        }

                        Row {
                            IconButton(onClick = {
                                selectedProdForPrice = p
                                showAddPriceDialog = true
                            }) {
                                Icon(Icons.Default.PriceChange, contentDescription = "Set Price", tint = MoroccanGold)
                            }
                            IconButton(onClick = { viewModel.deleteProduct(p.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        // Tab 2: Manage Sources & APIs
        if (activeAdminTab == 2) {
            items(allSources) { src ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(src.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = if (src.isActive) PriceDecreaseGreen.copy(0.12f) else Color.LightGray,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (src.isActive) "ACTIVE" else "DISABLED",
                                        color = if (src.isActive) PriceDecreaseGreen else Color.DarkGray,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("نوع المصدر: ${src.type} • تحديث: ${src.updateFrequency}", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                            Text("Endpoint: ${src.endpointUrl}", style = MaterialTheme.typography.labelSmall, color = MoroccanEmerald, maxLines = 1)
                        }

                        Switch(
                            checked = src.isActive,
                            onCheckedChange = { viewModel.togglePriceSource(src) },
                            colors = SwitchDefaults.colors(checkedThumbColor = MoroccanEmerald)
                        )
                    }
                }
            }
        }

        // Tab 3: Audit Logs
        if (activeAdminTab == 3) {
            items(auditLogs) { log ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(log.action, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, color = MoroccanTerracotta)
                            Text(log.actorEmail, style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(log.details, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }

    // Add Product Dialog
    if (showAddProductDialog) {
        AlertDialog(
            onDismissRequest = { showAddProductDialog = false },
            title = { Text("إضافة منتج مغربي جديد") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = newProdNameAr, onValueChange = { newProdNameAr = it }, label = { Text("الاسم بالعربية") }, singleLine = true)
                    OutlinedTextField(value = newProdNameFr, onValueChange = { newProdNameFr = it }, label = { Text("Nom en Français") }, singleLine = true)
                    OutlinedTextField(value = newProdNameEn, onValueChange = { newProdNameEn = it }, label = { Text("Name in English") }, singleLine = true)
                    OutlinedTextField(value = newProdCategory, onValueChange = { newProdCategory = it }, label = { Text("الصنف / Catégorie") }, singleLine = true)
                    OutlinedTextField(value = newProdBarcode, onValueChange = { newProdBarcode = it }, label = { Text("الباركود (EAN-13)") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newProdNameAr.isNotBlank()) {
                            viewModel.addNewProduct(
                                ProductEntity(
                                    id = "prod-user-" + UUID.randomUUID().toString().take(6),
                                    nameAr = newProdNameAr,
                                    nameFr = newProdNameFr.ifEmpty { newProdNameAr },
                                    nameEn = newProdNameEn.ifEmpty { newProdNameAr },
                                    category = newProdCategory,
                                    barcode = newProdBarcode,
                                    unit = "kg"
                                )
                            )
                        }
                        showAddProductDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MoroccanEmerald)
                ) {
                    Text("حفظ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddProductDialog = false }) { Text("إلغاء") }
            }
        )
    }

    // Add Price Dialog
    if (showAddPriceDialog && selectedProdForPrice != null) {
        val prod = selectedProdForPrice!!
        AlertDialog(
            onDismissRequest = { showAddPriceDialog = false },
            title = { Text("تحديد سعر معتمد لـ ${prod.nameAr}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = priceInput, onValueChange = { priceInput = it }, label = { Text("السعر بالدرهم (MAD)") }, singleLine = true)
                    OutlinedTextField(value = priceCityInput, onValueChange = { priceCityInput = it }, label = { Text("المدينة") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val p = priceInput.toDoubleOrNull()
                        if (p != null) {
                            viewModel.addPriceRecord(
                                PriceRecordEntity(
                                    productId = prod.id,
                                    price = p,
                                    currency = "MAD",
                                    unit = prod.unit,
                                    city = priceCityInput,
                                    marketSource = "Marché de Gros / Survey",
                                    sourceName = "Admin Verified Entry",
                                    date = "2026-09-20",
                                    verificationStatus = "VERIFIED"
                                )
                            )
                        }
                        showAddPriceDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MoroccanEmerald)
                ) {
                    Text("تسجيل السعر")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPriceDialog = false }) { Text("إلغاء") }
            }
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = MoroccanEmerald)
            Text(title, style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark, fontSize = 10.sp)
        }
    }
}
