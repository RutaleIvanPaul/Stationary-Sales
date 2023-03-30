package io.ramani.ramaniStationary.domain.createorder.useCase

import io.ramani.ramaniStationary.data.createorder.models.request.SaleRequestModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createorder.CreateOrderDataSource
import io.ramani.ramaniStationary.domain.createorder.model.SaleModel
import io.reactivex.Single

class PostNewSaleUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val createOrderDataSource: CreateOrderDataSource
): BaseSingleUseCase<SaleModel, SaleRequestModel>(threadExecutor,postThreadExecutor) {

    override fun buildUseCaseSingle(params: SaleRequestModel?): Single<SaleModel> =
        createOrderDataSource.postNewSale(params!!)

}