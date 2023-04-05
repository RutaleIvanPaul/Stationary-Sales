package io.ramani.ramaniStationary.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.ramani.ramaniStationary.data.home.database.MerchantDao
import io.ramani.ramaniStationary.data.home.database.ProductDao
import io.ramani.ramaniStationary.data.home.database.TaxDao
import io.ramani.ramaniStationary.data.home.database.converter.MerchantMemberConverts
import io.ramani.ramaniStationary.data.home.database.converter.ProductCategoryConverts
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel

const val DATABASE_NAME = "ramani_stationary_app_db"
const val DATABASE_VERSION = 1

@Database(
    entities = [MerchantModel::class, ProductModel::class, TaxModel::class],
    version = DATABASE_VERSION
)
@TypeConverters(ProductCategoryConverts::class, MerchantMemberConverts::class)
abstract class RamaniDatabase : RoomDatabase() {
    abstract fun getMerchantDao(): MerchantDao
    abstract fun getProductDao(): ProductDao
    abstract fun getTaxDao(): TaxDao
}