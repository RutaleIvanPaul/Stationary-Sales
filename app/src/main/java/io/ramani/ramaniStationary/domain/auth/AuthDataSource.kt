package io.ramani.ramaniStationary.domain.auth

import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.reactivex.Completable
import io.reactivex.Single

interface AuthDataSource {
    fun login(phone: String, password: String): Single<UserModel>
    fun getCurrentUser(): Single<UserModel>
    fun setCurrentUser(user: UserModel): Completable
    fun logout(): Single<Any>
    fun refreshAccessToken(token: String): Completable

}