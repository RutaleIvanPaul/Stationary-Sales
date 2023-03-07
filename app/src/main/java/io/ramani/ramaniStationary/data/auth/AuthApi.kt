package io.ramani.ramaniStationary.data.auth

import io.ramani.ramaniStationary.data.auth.models.LoginRequestModel
import io.ramani.ramaniStationary.data.auth.models.UserRemoteModel
import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/login/single-session/03-11-2022")
    fun login(
        @Body loginRequest: LoginRequestModel
    ): Single<BaseResponse<UserRemoteModel>>

    @POST("auth/10-11-2022/logout")
    fun logout(): Single<BaseResponse<Any>>

}