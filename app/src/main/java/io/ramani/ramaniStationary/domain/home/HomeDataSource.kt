package io.ramani.ramaniStationary.domain.home

import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.reactivex.Single

interface HomeDataSource {
    fun getDailySalesStats(companyId: String, page: Int, size: Int, startDate: String, endDate: String): Single<List<DailySalesStatsModel>>
    fun getTaxes(companyId: String, userId: String, date: String, page: Int, size: Int): Single<List<TaxModel>>
    fun getProducts(date: String, archived: Boolean, page: Int, size: Int): Single<List<ProductModel>>
    fun getMerchants(date: String, isActive: Boolean, page: Int, size: Int): Single<List<MerchantModel>>

}