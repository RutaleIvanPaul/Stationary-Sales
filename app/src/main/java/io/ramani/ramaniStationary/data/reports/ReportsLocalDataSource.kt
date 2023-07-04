package io.ramani.ramaniStationary.data.reports

import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.database.RamaniDatabase
import io.ramani.ramaniStationary.domain.reports.ReportsDataSource
import io.ramani.ramaniStationary.domain.reports.model.SalesSummaryStatisticsModel
import io.ramani.ramaniStationary.domainCore.prefs.Prefs
import io.reactivex.Single

class ReportsLocalDataSource(
    private val prefsManager: Prefs,
    private val db: RamaniDatabase
) : ReportsDataSource, BaseRemoteDataSource() {

    override fun getSalesSummaryStatistics(companyId: String, salesPersonUID: String, page: Int, startDate: String, endDate: String): Single<SalesSummaryStatisticsModel> {
        TODO("Not yet implemented")
    }

}