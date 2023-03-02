package io.ramani.ramaniStationary.domain.home.useCase

import io.ramani.ramaniStationary.data.home.models.request.DailySalesStatsRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetProductRequestModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel

class GetProductsUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val homeDataSource: HomeDataSource
) : BaseSingleUseCase<List<ProductModel>, GetProductRequestModel>(
    threadExecutor,
    postThreadExecutor
) {

    override fun buildUseCaseSingle(params: GetProductRequestModel?) =
        homeDataSource.getProducts(params!!.date, params.archived, params.page, params.size)

}