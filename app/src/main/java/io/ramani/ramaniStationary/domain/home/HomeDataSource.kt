package io.ramani.ramaniStationary.domain.home

import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.reactivex.Single

interface HomeDataSource {
    fun getDailySalesStats(date: String, companyId: String, page: Int, size: Int, startDate: String, endDate: String): Single<List<DailySalesStatsModel>>

}