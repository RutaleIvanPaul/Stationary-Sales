package io.ramani.ramaniStationary.domain.createmerchant

import io.ramani.ramaniStationary.data.createmerchant.models.request.RegisterMerchantRequestModel
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.reactivex.Maybe
import io.reactivex.Single

interface CreateMerchantDataSource {
    fun getTopPerformers(companyId: String, salesPersonUID: String, startDate: String, endDate: String, size: Int): Single<TopPerformersModel>
    fun registerMerchant(merchant: RegisterMerchantRequestModel): Single<MerchantModel>
    fun saveMerchant(merchant: MerchantModel): Maybe<Long>
}