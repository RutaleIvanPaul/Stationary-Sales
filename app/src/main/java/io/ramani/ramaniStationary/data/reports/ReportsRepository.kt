package io.ramani.ramaniStationary.data.reports

import io.ramani.ramaniStationary.domain.reports.ReportsDataSource
import io.ramani.ramaniStationary.domain.reports.model.SalesSummaryStatisticsModel
import io.reactivex.Single

class ReportsRepository(
    private val remoteReportsDataSource: ReportsDataSource,
    private val localReportsDataSource: ReportsDataSource
): ReportsDataSource {

    override fun getSalesSummaryStatistics(companyId: String, page: Int, startDate: String, endDate: String): Single<SalesSummaryStatisticsModel> =
        remoteReportsDataSource.getSalesSummaryStatistics(companyId, page, startDate, endDate)

}