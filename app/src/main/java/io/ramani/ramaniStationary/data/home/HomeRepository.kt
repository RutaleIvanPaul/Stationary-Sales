package io.ramani.ramaniStationary.data.home

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

    override fun getDailySalesStats(companyId: String, page: Int, size: Int, startDate: String, endDate: String): Single<List<DailySalesStatsModel>>  =
        remoteAuthDataSource.getDailySalesStats(companyId, page, size, startDate, endDate)

    override fun getTaxes(companyId: String, userId: String, date: String, page: Int, size: Int): Single<List<TaxModel>>  =
        remoteAuthDataSource.getTaxes(companyId, userId, date, page, size)

    override fun getProducts(date: String, archived: Boolean, page: Int, size: Int): Single<List<ProductModel>>  =
        remoteAuthDataSource.getProducts(date, archived, page, size)

    override fun getMerchants(date: String, isActive: Boolean, page: Int, size: Int): Single<List<MerchantModel>>  =
        remoteAuthDataSource.getMerchants(date, isActive, page, size)

}