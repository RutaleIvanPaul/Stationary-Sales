package io.ramani.ramaniStationary.data.home.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface TaxDao {

    @Query("SELECT * FROM Tax")
    fun getTaxes(): Single<List<TaxModel>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(tax: TaxModel):Maybe<Long>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(taxes: List<TaxModel>): List<Long>

    @Query("DELETE FROM Tax")
    fun deleteAll()

    @Query("SELECT COUNT(*) from Tax")
    fun countTaxes(): Int
}
