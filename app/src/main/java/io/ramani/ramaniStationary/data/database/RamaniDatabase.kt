package io.ramani.ramaniStationary.data.database

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import io.ramani.ramaniStationary.data.createorder.database.SaleDao
import io.ramani.ramaniStationary.data.createorder.database.SaleJsonModel
import io.ramani.ramaniStationary.data.createorder.database.converter.SaleOrderItemModelConverts
import io.ramani.ramaniStationary.data.createorder.database.converter.SaleOrderModelConverts
import io.ramani.ramaniStationary.data.createorder.database.converter.StringConverts
import io.ramani.ramaniStationary.data.createorder.models.request.SaleRequestModel
import io.ramani.ramaniStationary.data.home.database.MerchantDao
import io.ramani.ramaniStationary.data.home.database.ProductDao
import io.ramani.ramaniStationary.data.home.database.TaxDao
import io.ramani.ramaniStationary.data.home.database.converter.MerchantMemberConverts
import io.ramani.ramaniStationary.data.home.database.converter.ProductCategoryConverts
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel

const val DATABASE_NAME = "ramani_stationary_app_db"
const val DATABASE_VERSION = 3
@Database(
    version = DATABASE_VERSION,
    entities = [MerchantModel::class, ProductModel::class, TaxModel::class, SaleJsonModel::class],
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2
        ),
        AutoMigration(
            from = 2,
            to = 3
        )
    ],
    exportSchema = true
)
@TypeConverters(ProductCategoryConverts::class, MerchantMemberConverts::class)
abstract class RamaniDatabase : RoomDatabase() {
    abstract fun getMerchantDao(): MerchantDao
    abstract fun getProductDao(): ProductDao
    abstract fun getTaxDao(): TaxDao
    abstract fun getSaleDao(): SaleDao
}