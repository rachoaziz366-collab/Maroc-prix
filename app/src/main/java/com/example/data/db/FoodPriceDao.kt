package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AuditLogEntity
import com.example.data.model.FavoriteEntity
import com.example.data.model.PriceAlertEntity
import com.example.data.model.PriceHistoryEntity
import com.example.data.model.PriceRecordEntity
import com.example.data.model.PriceSourceEntity
import com.example.data.model.ProductEntity
import com.example.data.model.UserSubmissionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodPriceDao {

    // --- Products ---
    @Query("SELECT * FROM products ORDER BY nameAr ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: String): ProductEntity?

    @Query("SELECT * FROM products WHERE category = :category ORDER BY nameAr ASC")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isLocalTerroir = 1 ORDER BY nameAr ASC")
    fun getTerroirProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isPackaged = 1 ORDER BY nameAr ASC")
    fun getPackagedProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE barcode = :barcode LIMIT 1")
    suspend fun getProductByBarcode(barcode: String): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProduct(productId: String)

    // --- Price Records ---
    @Query("SELECT * FROM price_records ORDER BY timestamp DESC")
    fun getAllPrices(): Flow<List<PriceRecordEntity>>

    @Query("SELECT * FROM price_records WHERE productId = :productId AND city = :city ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestPrice(productId: String, city: String): PriceRecordEntity?

    @Query("SELECT * FROM price_records WHERE productId = :productId ORDER BY timestamp DESC")
    fun getPricesForProduct(productId: String): Flow<List<PriceRecordEntity>>

    @Query("SELECT * FROM price_records WHERE city = :city ORDER BY timestamp DESC")
    fun getPricesForCity(city: String): Flow<List<PriceRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceRecord(record: PriceRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceRecords(records: List<PriceRecordEntity>)

    @Query("DELETE FROM price_records WHERE id = :id")
    suspend fun deletePriceRecord(id: String)

    // --- Price History ---
    @Query("SELECT * FROM price_history WHERE productId = :productId AND city = :city ORDER BY timestamp ASC")
    fun getPriceHistory(productId: String, city: String): Flow<List<PriceHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceHistory(records: List<PriceHistoryEntity>)

    // --- Sources ---
    @Query("SELECT * FROM price_sources ORDER BY name ASC")
    fun getAllSources(): Flow<List<PriceSourceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSource(source: PriceSourceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSources(sources: List<PriceSourceEntity>)

    @Update
    suspend fun updateSource(source: PriceSourceEntity)

    @Query("DELETE FROM price_sources WHERE id = :id")
    suspend fun deleteSource(id: String)

    // --- User Submissions ---
    @Query("SELECT * FROM user_submissions ORDER BY timestamp DESC")
    fun getAllSubmissions(): Flow<List<UserSubmissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(submission: UserSubmissionEntity)

    @Query("UPDATE user_submissions SET status = :status, adminNotes = :notes WHERE id = :submissionId")
    suspend fun updateSubmissionStatus(submissionId: String, status: String, notes: String?)

    @Query("DELETE FROM user_submissions WHERE id = :id")
    suspend fun deleteSubmission(id: String)

    // --- Favorites ---
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE productId = :productId")
    suspend fun deleteFavorite(productId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE productId = :productId)")
    fun isFavorite(productId: String): Flow<Boolean>

    // --- Price Alerts ---
    @Query("SELECT * FROM price_alerts ORDER BY createdAt DESC")
    fun getAllAlerts(): Flow<List<PriceAlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: PriceAlertEntity)

    @Query("DELETE FROM price_alerts WHERE id = :id")
    suspend fun deleteAlert(id: String)

    // --- Audit Logs ---
    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC LIMIT 50")
    fun getRecentAuditLogs(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLogEntity)
}
