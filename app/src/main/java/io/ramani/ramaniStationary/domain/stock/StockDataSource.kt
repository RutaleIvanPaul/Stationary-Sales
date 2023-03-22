package io.ramani.ramaniStationary.domain.stock

import io.ramani.ramaniStationary.data.stock.models.response.GetRollingStock
import io.reactivex.Single

interface StockDataSource {
    fun getAvailableStock(salesPersonUID: String): Single<GetRollingStock?>
}