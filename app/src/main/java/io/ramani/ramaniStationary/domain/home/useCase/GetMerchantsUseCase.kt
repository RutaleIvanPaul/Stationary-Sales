package io.ramani.ramaniStationary.domain.home.useCase

import io.ramani.ramaniStationary.data.home.models.request.GetMerchantRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetTaxRequestModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel

class GetMerchantsUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val homeDataSource: HomeDataSource
) : BaseSingleUseCase<PagedList<MerchantModel>, GetMerchantRequestModel>(
    threadExecutor,
    postThreadExecutor
) {

    override fun buildUseCaseSingle(params: GetMerchantRequestModel?) =
        homeDataSource.getMerchants(params!!.startDate, params.endDate, params.isActive, params.page)

}