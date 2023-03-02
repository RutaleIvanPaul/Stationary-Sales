package io.ramani.ramaniStationary.data.home

import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.ramani.ramaniStationary.domainCore.prefs.Prefs
import io.reactivex.Single

class HomeLocalDataSource(
    private val prefsManager: Prefs
) : HomeDataSource, BaseRemoteDataSource() {

    override fun getDailySalesStats(companyId: String, page: Int, size: Int, startDate: String, endDate: String): Single<List<DailySalesStatsModel>> {
        TODO("Not yet implemented")
    }
    override fun getTaxes(companyId: String, userId: String, date: String, page: Int, size: Int): Single<List<TaxModel>> {
        TODO("Not yet implemented")
    }

    override fun getProducts(date: String, archived: Boolean, page: Int, size: Int ): Single<List<ProductModel>> {
        TODO("Not yet implemented")
    }

    override fun getMerchants(date: String, isActive: Boolean, page: Int, size: Int ): Single<List<MerchantModel>> {
        TODO("Not yet implemented")
    }

}