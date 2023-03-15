package io.ramani.ramaniStationary.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.reactivex.Maybe

@Dao
interface TaxDao {

    @Query("SELECT * FROM Tax")
    fun getTaxes(): List<TaxModel>

    @Insert //(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tax: TaxModel):Maybe<Long>

    @Insert //(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(taxes: List<TaxModel>): List<Long>

    @Query("DELETE FROM Tax")
    fun deleteAll()

    @Query("SELECT COUNT(*) from Tax")
    fun countTaxes(): Int
}
