package io.ramani.ramaniStationary.data.createorder

import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.createorder.models.request.SaleRequestModel
import io.ramani.ramaniStationary.data.database.RamaniDatabase
import io.ramani.ramaniStationary.domain.createorder.CreateOrderDataSource
import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel
import io.ramani.ramaniStationary.domain.createorder.model.SaleModel
import io.ramani.ramaniStationary.domainCore.prefs.Prefs
import io.reactivex.Single

class CreateOrderLocalDataSource(
    private val prefsManager: Prefs,
    private val db: RamaniDatabase
) : CreateOrderDataSource, BaseRemoteDataSource() {

    override fun getAvailableStock(salesPersonUID: String): Single<List<AvailableStockModel>> {
        TODO("Not yet implemented")
    }

    override fun postNewSale(sale: SaleRequestModel): Single<SaleModel> {
        TODO("Not yet implemented")
    }
}