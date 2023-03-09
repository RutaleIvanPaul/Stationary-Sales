package io.ramani.ramaniStationary.data.home

import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.reactivex.Single

class HomeRepository(
    private val remoteAuthDataSource: HomeDataSource,
    private val localAuthDataSource: HomeDataSource
): HomeDataSource {

    override fun getDailySalesStats(companyId: String, page: Int, startDate: String, endDate: String): Single<PagedList<DailySalesStatsModel>>  =
        remoteAuthDataSource.getDailySalesStats(companyId, page, startDate, endDate)

    override fun getTaxes(companyId: String, userId: String, date: String, page: Int): Single<PagedList<TaxModel>>  =
        remoteAuthDataSource.getTaxes(companyId, userId, date, page)

    override fun getProducts(companyId: String, date: String, archived: Boolean, page: Int): Single<PagedList<ProductModel>>  =
        remoteAuthDataSource.getProducts(companyId, date, archived, page)

    override fun getMerchants(date: String, isActive: Boolean, page: Int): Single<PagedList<MerchantModel>>  =
        remoteAuthDataSource.getMerchants(date, isActive, page)

}