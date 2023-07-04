package io.ramani.ramaniStationary.domain.credit.useCase

import io.ramani.ramaniStationary.data.credit.models.request.GetLocationsRequestModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.credit.CreditDataSource
import io.ramani.ramaniStationary.domain.credit.model.LocationModel
import io.ramani.ramaniStationary.domain.entities.PagedList

class GetListLocationsUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val creditDataSource: CreditDataSource
) : BaseSingleUseCase<PagedList<LocationModel>, GetLocationsRequestModel>(
    threadExecutor,
    postThreadExecutor
) {

    override fun buildUseCaseSingle(params: GetLocationsRequestModel?) =
        creditDataSource.getListLocations(params!!.invalidateCache, params.sellerId, params.page)

}