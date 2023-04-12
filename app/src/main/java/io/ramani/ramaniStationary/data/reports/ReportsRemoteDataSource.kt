package io.ramani.ramaniStationary.data.reports

import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.reports.models.response.SalesSummaryStatisticsRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.base.mappers.mapFromWith
import io.ramani.ramaniStationary.domain.entities.exceptions.ParseResponseException
import io.ramani.ramaniStationary.domain.reports.ReportsDataSource
import io.ramani.ramaniStationary.domain.reports.model.SalesSummaryStatisticsModel
import io.reactivex.Single

class ReportsRemoteDataSource(
    private val reportsApi: ReportsApi,
    private val salesSummaryStatisticsRemoteMapper: ModelMapper<SalesSummaryStatisticsRemoteModel, SalesSummaryStatisticsModel>
) : ReportsDataSource, BaseRemoteDataSource() {

    override fun getSalesSummaryStatistics(companyId: String, page: Int, startDate: String, endDate: String): Single<SalesSummaryStatisticsModel> =
        callSingle(
            reportsApi.getSalesSummaryStatistics(companyId, page, startDate, endDate).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(salesSummaryStatisticsRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
                }
            }
        )

}