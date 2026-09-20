package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.ui.ChatMessage
import com.example.ui.MainViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AiAssistantScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    var userPromptInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val quickQuestions = when (language) {
        AppLanguage.ARABIC -> listOf(
            "ما هو سعر الطماطم اليوم في الدار البيضاء؟",
            "قارن بين أسعار السردين بين كازا وآسفي",
            "ما هي مكونات أملو الأمازيغي التقليدي؟",
            "كم يبلغ سعر زيت أركان AOP المعتمد؟"
        )
        AppLanguage.FRENCH -> listOf(
            "Quel est le prix de la tomate à Casablanca ?",
            "Compare le prix des sardines entre Safi et Casablanca",
            "Quels sont les bienfaits et ingrédients de l'Amlou ?",
            "Quel est le prix de l'huile d'argan AOP certifiée ?"
        )
        AppLanguage.ENGLISH -> listOf(
            "What is the current tomato price in Casablanca?",
            "Compare sardine prices between Safi and Casablanca",
            "What are the traditional ingredients of Berber Amlou?",
            "What is the certified price of Argan oil AOP?"
        )
    }

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("ai_assistant_screen")
    ) {
        // Disclaimer banner
        Surface(
            color = MoroccanEmeraldContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = MoroccanOnEmeraldContainer,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.get("ai_disclaimer", language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MoroccanOnEmeraldContainer,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        // Chat conversation
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (chatMessages.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MoroccanEmerald.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MoroccanEmerald, modifier = Modifier.size(36.dp))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = AppStrings.get("ai_assistant", language),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "المساعد الرقمي المعتمد لأسعار المنتجات الفلاحية والغذائية بالمملكة المغربية",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "أسئلة مقترحة سريعة:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MoroccanTerracotta
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        quickQuestions.forEach { q ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = CardDefaults.outlinedCardBorder(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        viewModel.sendAiQuestion(q)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("💡", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(q, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }

            items(chatMessages) { msg ->
                val isUser = msg.sender == "user"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 2.dp,
                            bottomEnd = if (isUser) 2.dp else 16.dp
                        ),
                        color = if (isUser) MoroccanEmerald else MaterialTheme.colorScheme.surface,
                        border = if (isUser) null else CardDefaults.outlinedCardBorder(),
                        modifier = Modifier.widthIn(max = 300.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            if (isAiLoading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = MoroccanEmerald
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("جاري فحص قاعدة البيانات المعتمدة...", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                            }
                        }
                    }
                }
            }
        }

        // Prompt Input
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            border = CardDefaults.outlinedCardBorder()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = userPromptInput,
                    onValueChange = { userPromptInput = it },
                    placeholder = { Text(AppStrings.get("ai_prompt_hint", language), fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MoroccanEmerald,
                        unfocusedBorderColor = BorderSoft
                    ),
                    maxLines = 3
                )

                IconButton(
                    onClick = {
                        if (userPromptInput.isNotBlank()) {
                            val p = userPromptInput.trim()
                            userPromptInput = ""
                            viewModel.sendAiQuestion(p)
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MoroccanEmerald)
                        .testTag("ai_send_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                }
            }
        }
    }
}
