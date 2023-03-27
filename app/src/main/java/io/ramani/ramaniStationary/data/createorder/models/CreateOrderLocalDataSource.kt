package io.ramani.ramaniStationary.data.createorder.models

import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.database.RamaniDatabase
import io.ramani.ramaniStationary.domain.createorder.CreateOrderDataSource
import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel
import io.ramani.ramaniStationary.domainCore.prefs.Prefs
import io.reactivex.Single

class CreateOrderLocalDataSource(
    private val prefsManager: Prefs,
    private val db: RamaniDatabase
) : CreateOrderDataSource, BaseRemoteDataSource() {

    override fun getAvailableStock(salesPersonUID: String): Single<List<AvailableStockModel>> {
        TODO("Not yet implemented")
    }
}