package io.ramani.ramaniStationary.data.home

import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.ramani.ramaniStationary.domainCore.prefs.Prefs
import io.reactivex.Single

class HomeLocalDataSource(
    private val prefsManager: Prefs
) : HomeDataSource, BaseRemoteDataSource() {

    override fun getDailySalesStats(date: String, companyId: String, page: Int, size: Int, startDate: String, endDate: String): Single<List<DailySalesStatsModel>> {
        TODO("Not yet implemented")
    }

}