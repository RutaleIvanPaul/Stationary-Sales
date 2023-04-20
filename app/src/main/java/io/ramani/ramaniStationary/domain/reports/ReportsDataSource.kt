package io.ramani.ramaniStationary.domain.reports

import io.ramani.ramaniStationary.domain.reports.model.SalesSummaryStatisticsModel
import io.reactivex.Single

interface ReportsDataSource {
    fun getSalesSummaryStatistics(companyId: String, salesPersonUID: String, page: Int, startDate: String, endDate: String): Single<SalesSummaryStatisticsModel>

}