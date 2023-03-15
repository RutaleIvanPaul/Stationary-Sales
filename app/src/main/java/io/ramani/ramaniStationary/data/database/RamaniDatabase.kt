package io.ramani.ramaniStationary.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel

const val DATABASE_NAME = "ramani_stationary_app_db"

@Database(entities = [MerchantModel::class, ProductModel::class, TaxModel::class], version = 1)
abstract class RamaniDatabase : RoomDatabase() {
    abstract fun getMerchantDao(): MerchantDao
    abstract fun getProductDao(): ProductDao
    abstract fun getTaxDao(): TaxDao
}