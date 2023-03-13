package io.ramani.ramaniStationary.domain.home

import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.reactivex.Single

interface HomeDataSource {
    fun getDailySalesStats(companyId: String, page: Int, startDate: String, endDate: String): Single<PagedList<DailySalesStatsModel>>
    fun getTaxes(companyId: String, userId: String, date: String, page: Int): Single<PagedList<TaxModel>>
    fun getProducts(companyId: String, startDate: String, endDate: String, archived: Boolean, page: Int): Single<PagedList<ProductModel>>
    fun getMerchants(startDate: String, endDate: String, isActive: Boolean, page: Int): Single<PagedList<MerchantModel>>

}