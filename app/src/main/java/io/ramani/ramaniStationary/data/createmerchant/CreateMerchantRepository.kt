package io.ramani.ramaniStationary.data.createmerchant

import io.ramani.ramaniStationary.data.createmerchant.models.request.RegisterMerchantRequestModel
import io.ramani.ramaniStationary.domain.createmerchant.CreateMerchantDataSource
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.reactivex.Maybe
import io.reactivex.Single

class CreateMerchantRepository(
    private val remoteCreateMerchantDataSource: CreateMerchantDataSource,
    private val localCreateMerchantDataSource: CreateMerchantDataSource
): CreateMerchantDataSource {

    override fun getTopPerformers(companyId: String, startDate: String, endDate: String, size: Int): Single<TopPerformersModel> =
        remoteCreateMerchantDataSource.getTopPerformers(companyId, startDate, endDate, size)

    override fun registerMerchant(merchant: RegisterMerchantRequestModel): Single<MerchantModel> =
        remoteCreateMerchantDataSource.registerMerchant(merchant).flatMap {
            saveMerchant(merchant = it)
            return@flatMap Single.just(it)
        }

    override fun saveMerchant(merchant: MerchantModel): Maybe<Long> =
        localCreateMerchantDataSource.saveMerchant(merchant)

}