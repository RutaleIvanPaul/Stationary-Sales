package io.ramani.ramaniStationary.domain.createorder

import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel
import io.reactivex.Single

interface CreateOrderDataSource {

    fun getAvailableStock(salesPersonUID: String): Single<List<AvailableStockModel>>

}