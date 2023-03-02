package io.ramani.ramaniStationary.data.home

import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.reactivex.Single

class HomeRepository(
    private val remoteAuthDataSource: HomeDataSource,
    private val localAuthDataSource: HomeDataSource
): HomeDataSource {

    override fun getDailySalesStats(date: String, companyId: String, page: Int, size: Int, startDate: String, endDate: String): Single<List<DailySalesStatsModel>>  =
        remoteAuthDataSource.getDailySalesStats(date, companyId, page, size, startDate, endDate)

}