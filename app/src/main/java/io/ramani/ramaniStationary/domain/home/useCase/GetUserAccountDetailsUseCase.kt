package io.ramani.ramaniStationary.domain.home.useCase

import io.ramani.ramaniStationary.data.home.models.request.GetUserAccountDetailsRequestModel
import io.ramani.ramaniStationary.domain.home.model.UserAccountDetailsModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.reactivex.Single

class GetUserAccountDetailsUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val homeDataSource: HomeDataSource
) : BaseSingleUseCase<List<UserAccountDetailsModel>, GetUserAccountDetailsRequestModel>(
    threadExecutor,
    postThreadExecutor
) {

    override fun buildUseCaseSingle(params: GetUserAccountDetailsRequestModel?): Single<List<UserAccountDetailsModel>> =
        homeDataSource.getAccountDetails(params!!.companyId)

}