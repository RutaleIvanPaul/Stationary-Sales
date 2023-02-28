package io.ramani.ramaniStationary.app.auth.di

import io.ramani.ramaniStationary.data.auth.AuthApi
import io.ramani.ramaniStationary.data.auth.AuthLocalDataSource
import io.ramani.ramaniStationary.data.auth.AuthRemoteDataSource
import io.ramani.ramaniStationary.data.auth.AuthRepository
import io.ramani.ramaniStationary.data.auth.mappers.UserRemoteMapper
import io.ramani.ramaniStationary.data.auth.models.UserRemoteModel
import io.ramani.ramaniStationary.data.common.network.ServiceHelper
import io.ramani.ramaniStationary.domain.auth.AuthDataSource
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.auth.manager.SessionManager
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val authDataModule = Kodein.Module("authDataModule") {

    bind<AuthApi>() with provider {
        ServiceHelper.createService<AuthApi>(instance())
    }

    bind<AuthDataSource>("authDataSource") with singleton {
        AuthRepository(
            instance("remoteAuthDataSource"),
            instance("localAuthDataSource")
        )
    }

    bind<AuthDataSource>("remoteAuthDataSource") with singleton {
        AuthRemoteDataSource(
            instance(),
            instance()
        )
    }

    bind<AuthDataSource>("localAuthDataSource") with singleton {
        AuthLocalDataSource(
            instance()
        )
    }

    bind<ModelMapper<UserRemoteModel, UserModel>>() with provider {
        UserRemoteMapper()
    }

    bind<ISessionManager>() with singleton {
        SessionManager(instance("authDataSource"))
    }
}