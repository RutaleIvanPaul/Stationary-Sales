package io.ramani.ramaniStationary.domain.credit.useCase

import io.ramani.ramaniStationary.data.credit.models.request.UpdateOrderPaymentStatusRequestModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.credit.CreditDataSource
import io.ramani.ramaniStationary.domain.entities.BaseResult

class UpdateOrderPaymentStatusUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val creditDataSource: CreditDataSource
) : BaseSingleUseCase<BaseResult, UpdateOrderPaymentStatusRequestModel>(
    threadExecutor,
    postThreadExecutor
) {

    override fun buildUseCaseSingle(params: UpdateOrderPaymentStatusRequestModel?) =
        creditDataSource.updateOrderPaymentStatus(params!!)

}