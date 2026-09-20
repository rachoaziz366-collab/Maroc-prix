package com.example.data.repository

import com.example.data.db.FoodPriceDao
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class FoodPriceRepository(private val dao: FoodPriceDao) {

    val allProducts: Flow<List<ProductEntity>> = dao.getAllProducts()
    val allPrices: Flow<List<PriceRecordEntity>> = dao.getAllPrices()
    val terroirProducts: Flow<List<ProductEntity>> = dao.getTerroirProducts()
    val packagedProducts: Flow<List<ProductEntity>> = dao.getPackagedProducts()
    val allSources: Flow<List<PriceSourceEntity>> = dao.getAllSources()
    val allSubmissions: Flow<List<UserSubmissionEntity>> = dao.getAllSubmissions()
    val allFavorites: Flow<List<FavoriteEntity>> = dao.getAllFavorites()
    val allAlerts: Flow<List<PriceAlertEntity>> = dao.getAllAlerts()
    val auditLogs: Flow<List<AuditLogEntity>> = dao.getRecentAuditLogs()

    suspend fun getProductById(id: String): ProductEntity? = withContext(Dispatchers.IO) {
        dao.getProductById(id)
    }

    suspend fun getProductByBarcode(barcode: String): ProductEntity? = withContext(Dispatchers.IO) {
        dao.getProductByBarcode(barcode)
    }

    fun getPricesForProduct(productId: String): Flow<List<PriceRecordEntity>> {
        return dao.getPricesForProduct(productId)
    }

    fun getPricesForCity(city: String): Flow<List<PriceRecordEntity>> {
        return dao.getPricesForCity(city)
    }

    fun getPriceHistory(productId: String, city: String): Flow<List<PriceHistoryEntity>> {
        return dao.getPriceHistory(productId, city)
    }

    fun isFavorite(productId: String): Flow<Boolean> = dao.isFavorite(productId)

    suspend fun toggleFavorite(productId: String) = withContext(Dispatchers.IO) {
        val currentFavs = dao.getAllFavorites().first()
        val exists = currentFavs.any { it.productId == productId }
        if (exists) {
            dao.deleteFavorite(productId)
        } else {
            dao.insertFavorite(FavoriteEntity(productId = productId))
        }
    }

    suspend fun insertProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        dao.insertProduct(product)
        dao.insertAuditLog(
            AuditLogEntity(
                actorEmail = "admin@moroccofood.ma",
                action = "CREATE_PRODUCT",
                entityType = "PRODUCT",
                details = "Added product: ${product.nameEn} / ${product.nameAr}"
            )
        )
    }

    suspend fun updateProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        dao.updateProduct(product)
        dao.insertAuditLog(
            AuditLogEntity(
                actorEmail = "admin@moroccofood.ma",
                action = "UPDATE_PRODUCT",
                entityType = "PRODUCT",
                details = "Updated product: ${product.nameEn}"
            )
        )
    }

    suspend fun deleteProduct(productId: String) = withContext(Dispatchers.IO) {
        dao.deleteProduct(productId)
        dao.insertAuditLog(
            AuditLogEntity(
                actorEmail = "admin@moroccofood.ma",
                action = "DELETE_PRODUCT",
                entityType = "PRODUCT",
                details = "Deleted product ID: $productId"
            )
        )
    }

    suspend fun insertPriceRecord(record: PriceRecordEntity) = withContext(Dispatchers.IO) {
        dao.insertPriceRecord(record)
        dao.insertAuditLog(
            AuditLogEntity(
                actorEmail = "admin@moroccofood.ma",
                action = "INSERT_PRICE",
                entityType = "PRICE_RECORD",
                details = "Set price for product ${record.productId} in ${record.city} to ${record.price} ${record.currency}"
            )
        )
    }

    suspend fun insertPriceSource(source: PriceSourceEntity) = withContext(Dispatchers.IO) {
        dao.insertSource(source)
    }

    suspend fun updatePriceSource(source: PriceSourceEntity) = withContext(Dispatchers.IO) {
        dao.updateSource(source)
    }

    suspend fun submitUserPrice(submission: UserSubmissionEntity) = withContext(Dispatchers.IO) {
        dao.insertSubmission(submission)
    }

    suspend fun moderateSubmission(submissionId: String, status: String, notes: String?) = withContext(Dispatchers.IO) {
        dao.updateSubmissionStatus(submissionId, status, notes)
        if (status == "APPROVED") {
            val subs = dao.getAllSubmissions().first()
            val sub = subs.find { it.id == submissionId }
            if (sub != null) {
                // If approved, create verified price record
                dao.insertPriceRecord(
                    PriceRecordEntity(
                        productId = sub.productId,
                        price = sub.price,
                        currency = "MAD",
                        unit = "kg",
                        city = sub.city,
                        marketSource = sub.storeName,
                        sourceName = "Verified Community Submission (${sub.submittedBy})",
                        date = sub.submissionDate,
                        verificationStatus = "VERIFIED"
                    )
                )
            }
        }
        dao.insertAuditLog(
            AuditLogEntity(
                actorEmail = "moderator@moroccofood.ma",
                action = "MODERATE_SUBMISSION",
                entityType = "SUBMISSION",
                details = "Moderated submission $submissionId with status $status"
            )
        )
    }

    suspend fun addPriceAlert(alert: PriceAlertEntity) = withContext(Dispatchers.IO) {
        dao.insertAlert(alert)
    }

    suspend fun deletePriceAlert(id: String) = withContext(Dispatchers.IO) {
        dao.deleteAlert(id)
    }

    suspend fun initializeDatabaseIfEmpty() = withContext(Dispatchers.IO) {
        val existing = dao.getAllProducts().first()
        if (existing.isEmpty()) {
            dao.insertProducts(PreloadedData.products)
            dao.insertPriceRecords(PreloadedData.priceRecords)
            dao.insertPriceHistory(PreloadedData.priceHistory)
            dao.insertSources(PreloadedData.sources)
            dao.insertSubmission(PreloadedData.sampleSubmission)
            dao.insertAuditLog(
                AuditLogEntity(
                    actorEmail = "system@moroccofood.ma",
                    action = "INITIALIZE_DATABASE",
                    entityType = "SYSTEM",
                    details = "Initialized database with Moroccan Food Price structures and verified sources"
                )
            )
        }
    }
}
