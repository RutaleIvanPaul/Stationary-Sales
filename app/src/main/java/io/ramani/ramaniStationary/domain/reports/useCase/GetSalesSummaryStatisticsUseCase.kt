package io.ramani.ramaniStationary.domain.reports.useCase

import io.ramani.ramaniStationary.data.reports.models.request.GetSalesSummaryStatisticsRequestModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.reports.ReportsDataSource
import io.ramani.ramaniStationary.domain.reports.model.SalesSummaryStatisticsModel

class GetSalesSummaryStatisticsUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val reportsDataSource: ReportsDataSource
) : BaseSingleUseCase<SalesSummaryStatisticsModel, GetSalesSummaryStatisticsRequestModel>(
    threadExecutor,
    postThreadExecutor
) {

    override fun buildUseCaseSingle(params: GetSalesSummaryStatisticsRequestModel?) =
        reportsDataSource.getSalesSummaryStatistics(params!!.companyId, params.page, params.startDate, params.endDate)

}