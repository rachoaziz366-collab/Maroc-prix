package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AuditLogEntity
import com.example.data.model.FavoriteEntity
import com.example.data.model.PriceAlertEntity
import com.example.data.model.PriceHistoryEntity
import com.example.data.model.PriceRecordEntity
import com.example.data.model.PriceSourceEntity
import com.example.data.model.ProductEntity
import com.example.data.model.UserSubmissionEntity

@Database(
    entities = [
        ProductEntity::class,
        PriceRecordEntity::class,
        PriceHistoryEntity::class,
        PriceSourceEntity::class,
        UserSubmissionEntity::class,
        FavoriteEntity::class,
        PriceAlertEntity::class,
        AuditLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FoodPriceDatabase : RoomDatabase() {

    abstract fun foodPriceDao(): FoodPriceDao

    companion object {
        @Volatile
        private var INSTANCE: FoodPriceDatabase? = null

        fun getDatabase(context: Context): FoodPriceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodPriceDatabase::class.java,
                    "morocco_food_prices.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
