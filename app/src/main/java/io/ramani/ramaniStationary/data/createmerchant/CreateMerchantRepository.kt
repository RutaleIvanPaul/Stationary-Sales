package io.ramani.ramaniStationary.data.createmerchant

import io.ramani.ramaniStationary.domain.createmerchant.CreateMerchantDataSource
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.reactivex.Single

class CreateMerchantRepository(
    private val remoteCreateMerchantDataSource: CreateMerchantDataSource,
    private val localCreateMerchantDataSource: CreateMerchantDataSource
): CreateMerchantDataSource {

    override fun getTopPerformers(companyId: String, startDate: String, endDate: String, size: Int): Single<TopPerformersModel> =
        remoteCreateMerchantDataSource.getTopPerformers(companyId, startDate, endDate, size)

}