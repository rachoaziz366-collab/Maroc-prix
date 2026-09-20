package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiAssistant
import com.example.data.model.*
import com.example.data.repository.FoodPriceRepository
import com.example.data.repository.PreloadedData
import com.example.localization.AppLanguage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class Screen {
    HOME,
    TERROIR,
    PACKAGED,
    COMPARE,
    FAVORITES,
    SCANNER,
    AI_ASSISTANT,
    ADMIN,
    SETTINGS,
    PRODUCT_DETAIL,
    SEARCH,
    SUBMIT_PRICE
}

enum class UserRole(val displayName: String) {
    USER("مستهلك / Utilisateur"),
    MODERATOR("مشرف / Modérateur"),
    DATA_EDITOR("محرر بيانات / Éditeur"),
    ADMIN("مدير / Administrateur"),
    SUPER_ADMIN("المدير العام / Super Admin")
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: String, // "user" or "assistant"
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

class MainViewModel(private val repository: FoodPriceRepository) : ViewModel() {

    // Language & City
    private val _currentLanguage = MutableStateFlow(AppLanguage.ARABIC)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _selectedCity = MutableStateFlow("Casablanca")
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    // Navigation & Selected detail
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _selectedProductId = MutableStateFlow<String?>(null)
    val selectedProductId: StateFlow<String?> = _selectedProductId.asStateFlow()

    // Filters
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // City Comparison State
    private val _compareCity1 = MutableStateFlow("Casablanca")
    val compareCity1: StateFlow<String> = _compareCity1.asStateFlow()

    private val _compareCity2 = MutableStateFlow("Mohammedia")
    val compareCity2: StateFlow<String> = _compareCity2.asStateFlow()

    // Current User Role
    private val _currentRole = MutableStateFlow(UserRole.ADMIN)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // AI Assistant
    private val aiAssistant = GeminiAssistant()
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Scanner state
    private val _scannedBarcode = MutableStateFlow("")
    val scannedBarcode: StateFlow<String> = _scannedBarcode.asStateFlow()

    private val _scannedProduct = MutableStateFlow<ProductEntity?>(null)
    val scannedProduct: StateFlow<ProductEntity?> = _scannedProduct.asStateFlow()

    // Data streams from repository
    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPrices: StateFlow<List<PriceRecordEntity>> = repository.allPrices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val terroirProducts: StateFlow<List<ProductEntity>> = repository.terroirProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val packagedProducts: StateFlow<List<ProductEntity>> = repository.packagedProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSources: StateFlow<List<PriceSourceEntity>> = repository.allSources
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSubmissions: StateFlow<List<UserSubmissionEntity>> = repository.allSubmissions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFavorites: StateFlow<List<FavoriteEntity>> = repository.allFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAlerts: StateFlow<List<PriceAlertEntity>> = repository.allAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AuditLogEntity>> = repository.auditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Combined product cards with prices for current city
    val productsWithPrices: StateFlow<List<ProductWithLatestPrice>> = combine(
        allProducts,
        allPrices,
        selectedCity
    ) { products, prices, city ->
        products.map { prod ->
            val cityPrices = prices.filter { it.productId == prod.id && it.city == city }
            val latest = cityPrices.firstOrNull()
            val previous = cityPrices.getOrNull(1)

            val priceChangePercent = if (latest?.price != null && previous?.price != null && previous.price!! > 0) {
                ((latest.price!! - previous.price!!) / previous.price!!) * 100
            } else null

            ProductWithLatestPrice(
                product = prod,
                currentPrice = latest?.price,
                previousPrice = previous?.price,
                currency = latest?.currency ?: "MAD",
                unit = prod.unit,
                city = city,
                lastUpdate = latest?.date ?: "Pending verification",
                verificationStatus = latest?.verificationStatus ?: "PENDING",
                sourceName = latest?.sourceName ?: "",
                priceChangePercent = priceChangePercent
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.initializeDatabaseIfEmpty()
        }
    }

    fun getPriceHistory(productId: String, city: String): Flow<List<PriceHistoryEntity>> {
        return repository.getPriceHistory(productId, city)
    }

    // Setters & Actions
    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun setSelectedCity(city: String) {
        _selectedCity.value = city
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun openProductDetail(productId: String) {
        _selectedProductId.value = productId
        _currentScreen.value = Screen.PRODUCT_DETAIL
    }

    fun setSelectedCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCompareCity1(city: String) {
        _compareCity1.value = city
    }

    fun setCompareCity2(city: String) {
        _compareCity2.value = city
    }

    fun setUserRole(role: UserRole) {
        _currentRole.value = role
    }

    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(productId)
        }
    }

    fun isProductFavorite(productId: String): Boolean {
        return allFavorites.value.any { it.productId == productId }
    }

    fun submitPrice(
        productId: String,
        productName: String,
        price: Double,
        city: String,
        storeName: String,
        submittedBy: String
    ) {
        viewModelScope.launch {
            repository.submitUserPrice(
                UserSubmissionEntity(
                    productId = productId,
                    productName = productName,
                    price = price,
                    city = city,
                    storeName = storeName,
                    submittedBy = submittedBy,
                    submissionDate = "2026-09-20",
                    status = "PENDING"
                )
            )
            _currentScreen.value = Screen.HOME
        }
    }

    fun moderateSubmission(submissionId: String, status: String, notes: String? = null) {
        viewModelScope.launch {
            repository.moderateSubmission(submissionId, status, notes)
        }
    }

    fun createPriceAlert(
        productId: String,
        productName: String,
        targetPrice: Double,
        city: String,
        condition: String
    ) {
        viewModelScope.launch {
            repository.addPriceAlert(
                PriceAlertEntity(
                    productId = productId,
                    productName = productName,
                    targetPrice = targetPrice,
                    city = city,
                    condition = condition
                )
            )
        }
    }

    fun deletePriceAlert(id: String) {
        viewModelScope.launch {
            repository.deletePriceAlert(id)
        }
    }

    fun addNewProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
        }
    }

    fun addPriceRecord(record: PriceRecordEntity) {
        viewModelScope.launch {
            repository.insertPriceRecord(record)
        }
    }

    fun togglePriceSource(source: PriceSourceEntity) {
        viewModelScope.launch {
            repository.updatePriceSource(source.copy(isActive = !source.isActive))
        }
    }

    // Barcode Lookup
    fun scanBarcode(code: String) {
        _scannedBarcode.value = code
        viewModelScope.launch {
            val prod = repository.getProductByBarcode(code)
            _scannedProduct.value = prod
        }
    }

    fun clearScanned() {
        _scannedBarcode.value = ""
        _scannedProduct.value = null
    }

    // AI Assistant Question
    fun sendAiQuestion(prompt: String) {
        val userMsg = ChatMessage(sender = "user", message = prompt)
        _chatMessages.value = _chatMessages.value + userMsg
        _isAiLoading.value = true

        viewModelScope.launch {
            val response = aiAssistant.answerQuestion(
                question = prompt,
                products = allProducts.value,
                prices = allPrices.value,
                language = _currentLanguage.value
            )
            val aiMsg = ChatMessage(sender = "assistant", message = response)
            _chatMessages.value = _chatMessages.value + aiMsg
            _isAiLoading.value = false
        }
    }
}
