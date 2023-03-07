package io.ramani.ramaniStationary.domain.home.useCase

import io.ramani.ramaniStationary.data.home.models.request.DailySalesStatsRequestModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel

class DailySalesStatsUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val homeDataSource: HomeDataSource
) : BaseSingleUseCase<PagedList<DailySalesStatsModel>, DailySalesStatsRequestModel>(
    threadExecutor,
    postThreadExecutor
) {

    override fun buildUseCaseSingle(params: DailySalesStatsRequestModel?) =
        homeDataSource.getDailySalesStats(params!!.companyId, params.page, params.startDate, params.endDate)

}