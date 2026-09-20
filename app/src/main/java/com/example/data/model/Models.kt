package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val nameAr: String,
    val nameFr: String,
    val nameEn: String,
    val category: String,
    val subcategory: String = "",
    val imageUrl: String = "",
    val descriptionAr: String = "",
    val descriptionFr: String = "",
    val descriptionEn: String = "",
    val unit: String = "kg", // kg, litre, pièce, boîte, etc.
    val weight: String = "",
    val manufacturer: String = "",
    val brand: String = "",
    val countryOfOrigin: String = "Morocco",
    val manufacturingLocation: String = "",
    val ingredients: String = "",
    val nutritionFacts: String = "",
    val allergens: String = "",
    val productBenefits: String = "",
    val barcode: String = "",
    val isLocalTerroir: Boolean = false,
    val isPackaged: Boolean = false,
    val region: String = "", // e.g. Souss-Massa, Tafilalet, Chaouia
    val productionMethod: String = "",
    val certification: String = "", // e.g. IGP, AOP, Bio Maroc
    val isOrganic: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "price_records")
data class PriceRecordEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val productId: String,
    val price: Double?, // null if price data is not available yet
    val currency: String = "MAD",
    val unit: String = "kg",
    val city: String,
    val region: String = "",
    val marketSource: String, // e.g. "Wholesale Market", "Central Market", "Supermarket"
    val sourceName: String, // e.g. "Ministry of Agriculture / Marché de Gros"
    val sourceUrl: String = "",
    val date: String,
    val time: String = "08:00",
    val verificationStatus: String = "VERIFIED", // "VERIFIED", "PENDING", "DEMO_DATA"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "price_history")
data class PriceHistoryEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val productId: String,
    val city: String,
    val price: Double,
    val date: String,
    val periodLabel: String, // "Current", "7 Days Ago", "30 Days Ago", "3 Months Ago", "6 Months Ago", "1 Year Ago"
    val timestamp: Long,
    val sourceName: String
)

@Entity(tableName = "price_sources")
data class PriceSourceEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: String, // "GOVERNMENT", "WHOLESALE", "RETAIL_PARTNER", "USER_COMMUNITY"
    val endpointUrl: String = "",
    val updateFrequency: String = "Daily", // Daily, Weekly, Real-time
    val isActive: Boolean = true,
    val reliabilityScore: Double = 0.95,
    val lastSyncDate: String = "Today"
)

@Entity(tableName = "user_submissions")
data class UserSubmissionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val productId: String,
    val productName: String,
    val price: Double,
    val city: String,
    val storeName: String,
    val photoUri: String? = null,
    val submittedBy: String = "user@moroccofood.ma",
    val submissionDate: String,
    val status: String = "PENDING", // "PENDING", "APPROVED", "REJECTED"
    val adminNotes: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val productId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "price_alerts")
data class PriceAlertEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val productId: String,
    val productName: String,
    val targetPrice: Double,
    val city: String,
    val condition: String = "BELOW", // "BELOW", "DECREASE", "CHANGE"
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val actorEmail: String,
    val action: String,
    val entityType: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class ProductWithLatestPrice(
    val product: ProductEntity,
    val currentPrice: Double?,
    val previousPrice: Double?,
    val currency: String = "MAD",
    val unit: String = "kg",
    val city: String = "Casablanca",
    val lastUpdate: String = "",
    val verificationStatus: String = "VERIFIED",
    val sourceName: String = "",
    val priceChangePercent: Double? = null
)
