package io.ramani.ramaniStationary.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.reactivex.Maybe

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun getProducts(): List<ProductModel>

    @Insert //(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: ProductModel):Maybe<Long>

    @Insert //(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<ProductModel>): List<Long>

    @Query("DELETE FROM Product")
    fun deleteAll()

    @Query("SELECT COUNT(*) from Product")
    fun countProducts(): Int
}
