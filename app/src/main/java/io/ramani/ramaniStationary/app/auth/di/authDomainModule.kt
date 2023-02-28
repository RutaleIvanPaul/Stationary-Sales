package io.ramani.ramaniStationary.app.auth.di

import io.ramani.ramaniStationary.data.auth.models.LoginRequestModel
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domain.auth.useCase.LoginUseCase
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val authDomainModule = Kodein.Module("authDomainModule") {
    bind<BaseSingleUseCase<UserModel, LoginRequestModel>>("loginUseCase") with provider {
        LoginUseCase(instance(), instance(), instance("authDataSource"))
    }
}