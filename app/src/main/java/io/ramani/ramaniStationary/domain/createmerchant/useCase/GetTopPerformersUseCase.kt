package io.ramani.ramaniStationary.domain.createmerchant.useCase

import io.ramani.ramaniStationary.data.createmerchant.models.request.GetTopPerformersRequestModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createmerchant.CreateMerchantDataSource
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.reactivex.Single

class GetTopPerformersUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val createMerchantDataSource: CreateMerchantDataSource
): BaseSingleUseCase<TopPerformersModel, GetTopPerformersRequestModel>(threadExecutor,postThreadExecutor) {

    override fun buildUseCaseSingle(params: GetTopPerformersRequestModel?): Single<TopPerformersModel> =
        createMerchantDataSource.getTopPerformers(params!!.companyId, params.salesPersonUID, params.startDate, params.endDate, params.size)

}