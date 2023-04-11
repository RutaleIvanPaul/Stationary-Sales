package io.ramani.ramaniStationary.domain.createmerchant.useCase

import io.ramani.ramaniStationary.data.createmerchant.models.request.RegisterMerchantRequestModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createmerchant.CreateMerchantDataSource
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.reactivex.Single

class RegisterMerchantUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val createMerchantDataSource: CreateMerchantDataSource
): BaseSingleUseCase<MerchantModel, RegisterMerchantRequestModel>(threadExecutor,postThreadExecutor) {

    override fun buildUseCaseSingle(params: RegisterMerchantRequestModel?): Single<MerchantModel> =
        createMerchantDataSource.registerMerchant(params!!)

}