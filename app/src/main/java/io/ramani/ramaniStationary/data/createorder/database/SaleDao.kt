package io.ramani.ramaniStationary.data.createorder.database

import androidx.room.*
import io.reactivex.Maybe

@Dao
interface SaleDao {

    @Query("SELECT * FROM Sales WHERE isSent=0 LIMIT 1")
    fun getUnsentSale(): SaleJsonModel

    @Query("SELECT * FROM Sales WHERE identify=:identify")
    fun getSale(identify: Long): SaleJsonModel

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(sale: SaleJsonModel):Maybe<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(sale: SaleJsonModel): Int

    //@Query("DELETE FROM Sales WHERE identify=:identify")
    //fun delete(identify: Long)

    @Delete
    fun delete(sale: SaleJsonModel)

    @Query("DELETE FROM Sales")
    fun deleteAll()

    @Query("SELECT COUNT(*) from Sales WHERE isSent=0")
    fun countUnsentSales(): Int
}
