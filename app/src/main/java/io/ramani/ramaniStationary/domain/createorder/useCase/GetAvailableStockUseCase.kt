package io.ramani.ramaniStationary.domain.createorder.useCase

import io.ramani.ramaniStationary.data.createorder.models.request.GetAvailableStockRequestModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createorder.CreateOrderDataSource
import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel
import io.reactivex.Single

class GetAvailableStockUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val createOrderDataSource: CreateOrderDataSource
): BaseSingleUseCase<List<AvailableStockModel>, GetAvailableStockRequestModel>(threadExecutor,postThreadExecutor) {

    override fun buildUseCaseSingle(params: GetAvailableStockRequestModel?): Single<List<AvailableStockModel>> =
        createOrderDataSource.getAvailableStock(params!!.salesPersonUID)

}