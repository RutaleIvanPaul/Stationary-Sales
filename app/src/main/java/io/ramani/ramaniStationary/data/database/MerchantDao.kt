package io.ramani.ramaniStationary.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.reactivex.Maybe

@Dao
interface MerchantDao {

    @Query("SELECT * FROM Merchant")
    fun getMerchants(): List<MerchantModel>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(merchant: MerchantModel):Maybe<Long>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(merchants: List<MerchantModel>): List<Long>

    @Query("DELETE FROM Merchant")
    fun deleteAll()

    @Query("SELECT COUNT(*) from Merchant")
    fun countMerchants(): Int
}
