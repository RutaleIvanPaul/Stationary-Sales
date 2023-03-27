package io.ramani.ramaniStationary.data.stock

import io.ramani.ramaniStationary.data.stock.models.response.GetRollingStock
import io.ramani.ramaniStationary.domain.stock.StockDataSource
import io.reactivex.Single

class StockRepository(
    private val stockRemoteDataSource: StockDataSource
): StockDataSource {
    override fun getAvailableStock(salesPersonUID: String): Single<GetRollingStock?> =
        stockRemoteDataSource.getAvailableStock(salesPersonUID)
}