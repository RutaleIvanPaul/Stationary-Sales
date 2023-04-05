package io.ramani.ramaniStationary.data.createmerchant

import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.database.RamaniDatabase
import io.ramani.ramaniStationary.domain.createmerchant.CreateMerchantDataSource
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.ramani.ramaniStationary.domainCore.prefs.Prefs
import io.reactivex.Single

class CreateMerchantLocalDataSource(
    private val prefsManager: Prefs,
    private val db: RamaniDatabase
) : CreateMerchantDataSource, BaseRemoteDataSource() {

    override fun getTopPerformers(companyId: String, startDate: String, endDate: String, size: Int): Single<TopPerformersModel> {
        TODO("Not yet implemented")
    }

}