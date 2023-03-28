package io.ramani.ramaniStationary.domain.auth.manager

import io.ramani.ramaniStationary.data.auth.models.TaxInformationResponse
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.reactivex.Completable
import io.reactivex.Single

interface ISessionManager {
    fun login(email: String, password: String): Completable

    fun refreshAccessToken(token: String): Completable

    fun isUserLoggedIn(): Single<Boolean>

    fun getLoggedInUser(): Single<UserModel>

    fun logout(): Single<Any>

    fun getTaxObject(userId : String):Single<TaxInformationResponse>


}