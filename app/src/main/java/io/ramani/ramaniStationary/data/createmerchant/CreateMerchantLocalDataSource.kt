package io.ramani.ramaniStationary.data.createmerchant

import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.createmerchant.models.request.RegisterMerchantRequestModel
import io.ramani.ramaniStationary.data.database.RamaniDatabase
import io.ramani.ramaniStationary.domain.createmerchant.CreateMerchantDataSource
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domainCore.prefs.Prefs
import io.reactivex.Maybe
import io.reactivex.Single

class CreateMerchantLocalDataSource(
    private val prefsManager: Prefs,
    private val db: RamaniDatabase
) : CreateMerchantDataSource, BaseRemoteDataSource() {

    override fun getTopPerformers(companyId: String, salesPersonUID: String, startDate: String, endDate: String, size: Int): Single<TopPerformersModel> {
        TODO("Not yet implemented")
    }

    override fun registerMerchant(merchant: RegisterMerchantRequestModel): Single<MerchantModel> {
        TODO("Not yet implemented")
    }

    override fun saveMerchant(merchant: MerchantModel): Maybe<Long> =
        db.getMerchantDao().insert(merchant)

}