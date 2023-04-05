package io.ramani.ramaniStationary.domain.createmerchant

import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.reactivex.Single

interface CreateMerchantDataSource {
    fun getTopPerformers(companyId: String, startDate: String, endDate: String, size: Int): Single<TopPerformersModel>
}