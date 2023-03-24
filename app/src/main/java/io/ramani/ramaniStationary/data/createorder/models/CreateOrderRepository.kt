package io.ramani.ramaniStationary.data.createorder.models

import io.ramani.ramaniStationary.domain.createorder.CreateOrderDataSource
import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.reactivex.Single

class CreateOrderRepository(
    private val remoteCreateOrderDataSource: CreateOrderDataSource,
    private val localCreateOrderDataSource: CreateOrderDataSource
): CreateOrderDataSource {

    override fun getAvailableStock(salesPersonUID: String): Single<List<AvailableStockModel>> =
        remoteCreateOrderDataSource.getAvailableStock(salesPersonUID)

}