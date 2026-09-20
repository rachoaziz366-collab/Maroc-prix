package com.example.ui.screens

import androidx.compose.foundation.background
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
import com.example.ui.Screen
import com.example.ui.UserRole
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val currentRole by viewModel.currentRole.collectAsState()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showCityDialog by remember { mutableStateOf(false) }
    var showRoleDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("settings_screen"),
        contentPadding = PaddingValues(bottom = 90.dp, top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = AppStrings.get("settings", language),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MoroccanEmerald
            )
            Text(
                text = "تخصيص اللغة والمدينة والأذونات والشفافية",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryDark
            )
        }

        // Language setting card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppStrings.get("language", language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    AppLanguage.values().forEach { lang ->
                        val isSelected = lang == language
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MoroccanEmeraldContainer else Color.Transparent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.setLanguage(lang) }
                                .padding(vertical = 4.dp)
                                .testTag("lang_choice_${lang.code}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = when (lang) {
                                            AppLanguage.ARABIC -> "🇲🇦 " + lang.displayName + " (RTL)"
                                            AppLanguage.FRENCH -> "🇫🇷 " + lang.displayName
                                            AppLanguage.ENGLISH -> "🇬🇧 " + lang.displayName
                                        },
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MoroccanOnEmeraldContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MoroccanEmerald)
                                }
                            }
                        }
                    }
                }
            }
        }

        // City setting card
        item {
            Card(
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
                        Column {
                            Text(
                                text = "المدينة الافتراضية (Default City)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = selectedCity,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MoroccanEmerald,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Button(
                            onClick = { showCityDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MoroccanEmerald),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("تغيير")
                        }
                    }
                }
            }
        }

        // Role simulation selector card
        item {
            Card(
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
                        Column {
                            Text(
                                text = AppStrings.get("user_role", language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${currentRole.name} (${currentRole.displayName})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MoroccanTerracotta,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Button(
                            onClick = { showRoleDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MoroccanTerracotta),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("تبديل")
                        }
                    }
                }
            }
        }

        // Direct shortcut to Admin dashboard
        item {
            Button(
                onClick = { viewModel.navigateTo(Screen.ADMIN) },
                colors = ButtonDefaults.buttonColors(containerColor = MoroccanEmerald),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("admin_dashboard_shortcut_btn")
            ) {
                Icon(Icons.Default.AdminPanelSettings, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(AppStrings.get("admin", language), fontWeight = FontWeight.Bold)
            }
        }

        // Integrity & Transparency notice
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MoroccanEmeraldContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🇲🇦", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ميثاق الشفافية والنزاهة لبيانات الأسعار",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MoroccanOnEmeraldContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "تلتزم منصة أسعار المواد الغذائية بالمغرب بعدم اختراع أو توليد أي أسعار وهمية. كل رقم معروض مستند إلى نشرات أسواق الجملة والوزارة أو مساهمات مواطنين خضعت للتدقيق والتوثيق.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MoroccanOnEmeraldContainer,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "للتواصل مع فريق التطوير: rachoaziz366@gmail.com",
                        style = MaterialTheme.typography.labelSmall,
                        color = MoroccanOnEmeraldContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }

    // City Selection Dialog
    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = { Text("اختر المدينة") },
            text = {
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    items(PreloadedData.moroccanCities) { c ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setSelectedCity(c.nameEn)
                                    showCityDialog = false
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            Text(
                                text = "${c.nameAr} (${c.nameEn})",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (c.nameEn == selectedCity) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) { Text("إغلاق") }
            }
        )
    }

    // Role Selection Dialog
    if (showRoleDialog) {
        AlertDialog(
            onDismissRequest = { showRoleDialog = false },
            title = { Text("اختر صفة المستخدم لتجربة الصلاحيات") },
            text = {
                Column {
                    UserRole.values().forEach { role ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setUserRole(role)
                                    showRoleDialog = false
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "${role.name} - ${role.displayName}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (role == currentRole) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRoleDialog = false }) { Text("إلغاء") }
            }
        )
    }
}
