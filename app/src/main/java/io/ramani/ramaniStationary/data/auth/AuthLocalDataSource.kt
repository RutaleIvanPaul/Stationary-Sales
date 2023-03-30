package io.ramani.ramaniStationary.data.auth

import com.google.gson.Gson
import io.ramani.ramaniStationary.data.auth.models.TaxInformationResponse
import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.domain.auth.AuthDataSource
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domainCore.prefs.Prefs
import io.reactivex.Completable
import io.reactivex.Single

class AuthLocalDataSource(
    private val prefsManager: Prefs
) : AuthDataSource, BaseRemoteDataSource() {
    override fun login(phone: String, password: String): Single<UserModel> {
        TODO("Not yet implemented")
    }


    override fun getCurrentUser(): Single<UserModel> =
        if (prefsManager.currentUser.isNullOrBlank()) {
            Single.just(UserModel())
        } else {
            Single.just(Gson().fromJson(prefsManager.currentUser, UserModel::class.java))
        }

    override fun setCurrentUser(user: UserModel): Completable =
        Completable.fromAction {
            prefsManager.currentUser = Gson().toJson(user)
        }

    override fun logout(): Single<Any> {
        prefsManager.currentUser = ""
        prefsManager.accessToken = ""
        prefsManager.currentWarehouse = ""
        prefsManager.taxObject = ""

        return Single.just(true)
    }

    override fun refreshAccessToken(token: String): Completable =
        Completable.fromAction {
            prefsManager.accessToken = token
        }

    override fun getTaxObject(): Single<TaxInformationResponse> =
        if (prefsManager.taxObject.isNullOrBlank()) {
            Single.just(null)
        } else {
            Single.just(Gson().fromJson(prefsManager.taxObject, TaxInformationResponse::class.java))
        }

    override fun getTaxObjectOnline(userId: String): Single<TaxInformationResponse> {
        TODO("Not yet implemented")
    }


}