package io.ramani.ramaniStationary.domain.auth.useCase

import io.ramani.ramaniStationary.data.auth.models.LoginRequestModel
import io.ramani.ramaniStationary.domain.auth.AuthDataSource
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.reactivex.Single

class LoginUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val authDataSource: AuthDataSource
) : BaseSingleUseCase<UserModel, LoginRequestModel>(
    threadExecutor,
    postThreadExecutor
) {
    override fun buildUseCaseSingle(params: LoginRequestModel?): Single<UserModel> =
        authDataSource.login(params?.phoneNumber ?: "", params?.password ?: "")

}