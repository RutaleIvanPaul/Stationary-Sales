package io.ramani.ramaniStationary.domain.auth.useCase

import io.ramani.ramaniStationary.data.auth.models.TaxInformationResponse
import io.ramani.ramaniStationary.data.auth.models.request.TaxInformationRequest
import io.ramani.ramaniStationary.domain.auth.AuthDataSource
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.reactivex.Single

class TaxObjectUseCase(threadExecutor: ThreadExecutor,
                       postThreadExecutor: PostThreadExecutor,
                       private val authDataSource: AuthDataSource
): BaseSingleUseCase<TaxInformationResponse, TaxInformationRequest>(
    threadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseSingle(params: TaxInformationRequest?): Single<TaxInformationResponse> =
        authDataSource.getTaxObject(params!!.userId)

}