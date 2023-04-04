package io.ramani.ramaniStationary.domain.home.useCase

import io.ramani.ramaniStationary.data.home.models.request.GetTaxInformationRequestModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.TaxInformationModel

class GetTaxInformationUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val homeDataSource: HomeDataSource
) : BaseSingleUseCase<TaxInformationModel, GetTaxInformationRequestModel>(
    threadExecutor,
    postThreadExecutor
) {

    override fun buildUseCaseSingle(params: GetTaxInformationRequestModel?) =
        homeDataSource.getTaxInformationByUserId(params!!.userId)

}