package io.ramani.ramaniStationary.data.auth

import io.ramani.ramaniStationary.domain.auth.AuthDataSource
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.reactivex.Completable
import io.reactivex.Single

class AuthRepository(
    private val remoteAuthDataSource: AuthDataSource,
    private val localAuthDataSource: AuthDataSource
) :
    AuthDataSource {
    override fun login(phone: String, password: String): Single<UserModel> =
        remoteAuthDataSource.login(phone, password)


    override fun getCurrentUser(): Single<UserModel> =
        localAuthDataSource.getCurrentUser()

    override fun setCurrentUser(user: UserModel): Completable =
        localAuthDataSource.setCurrentUser(user)

    override fun logout(): Single<Any> =
        localAuthDataSource.logout()
        /*
        remoteAuthDataSource.logout().flatMap {
            localAuthDataSource.logout()
        }
        */

    override fun refreshAccessToken(token: String): Completable =
        localAuthDataSource.refreshAccessToken(token)


}