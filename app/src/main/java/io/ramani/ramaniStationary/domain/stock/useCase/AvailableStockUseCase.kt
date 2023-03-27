package io.ramani.ramaniStationary.domain.stock.useCase

import io.ramani.ramaniStationary.data.stock.models.request.AvailableStockRequestModel
import io.ramani.ramaniStationary.data.stock.models.response.GetRollingStock
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.stock.StockDataSource
import io.reactivex.Single

class AvailableStockUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val stockDataSource: StockDataSource
) :BaseSingleUseCase<GetRollingStock?,AvailableStockRequestModel>(threadExecutor,postThreadExecutor){
    override fun buildUseCaseSingle(params: AvailableStockRequestModel?): Single<GetRollingStock?> =
        stockDataSource.getAvailableStock(params!!.salesPersonUID)

}