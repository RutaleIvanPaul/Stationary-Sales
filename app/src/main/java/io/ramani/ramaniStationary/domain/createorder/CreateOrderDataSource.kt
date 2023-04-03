package io.ramani.ramaniStationary.domain.createorder

import io.ramani.ramaniStationary.data.createorder.models.request.SaleRequestModel
import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel
import io.ramani.ramaniStationary.domain.createorder.model.SaleModel
import io.reactivex.Single

interface CreateOrderDataSource {
    fun getAvailableStock(salesPersonUID: String): Single<List<AvailableStockModel>>
    fun postNewSale(sale: SaleRequestModel): Single<SaleModel>
}