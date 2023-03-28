package io.ramani.ramaniStationary.data.auth

import io.ramani.ramaniStationary.data.auth.models.LoginRequestModel
import io.ramani.ramaniStationary.data.auth.models.TaxInformationResponse
import io.ramani.ramaniStationary.data.auth.models.UserRemoteModel
import io.ramani.ramaniStationary.data.common.network.ErrorConstants
import io.ramani.ramaniStationary.data.common.network.toErrorResponseModel
import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.domain.auth.AuthDataSource
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.base.mappers.mapFromWith
import io.ramani.ramaniStationary.domain.entities.BaseErrorResponse
import io.ramani.ramaniStationary.domain.entities.exceptions.AccountNotActiveException
import io.ramani.ramaniStationary.domain.entities.exceptions.InvalidLoginException
import io.ramani.ramaniStationary.domain.entities.exceptions.NotAuthorizedException
import io.ramani.ramaniStationary.domain.entities.exceptions.ParseResponseException
import io.ramani.ramaniStationary.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniStationary.domainCore.lang.isNotNull
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.HttpException

class AuthRemoteDataSource(
    private val authApi: AuthApi,
    private val userRemoteMapper: ModelMapper<UserRemoteModel, UserModel>,
) : AuthDataSource, BaseRemoteDataSource() {
    override fun login(phone: String, password: String): Single<UserModel> =
        callSingle(
            authApi.login(LoginRequestModel(phone, password)).flatMap {
                if (it.status == 200L || it.status == 201L) {
                    val data = it.data
                    if (data != null) {
                        Single.just(data.mapFromWith(userRemoteMapper))
                    } else {
                        Single.error(ParseResponseException())
                    }
                } else {
                    val message = "No active user with those credentials"
                    Single.error(
                        NotAuthorizedException(
                            message
                        )
                    )
                }
            }
                .onErrorResumeNext {
                    when (it) {
                        is HttpException -> {
                            val code = it.code()
                            val errorResponse = it.toErrorResponseModel<BaseErrorResponse<Any>>()
                            when (code) {
                                ErrorConstants.INPUT_VALIDATION_400,
                                ErrorConstants.NOT_FOUND_404 ->
                                    Single.error(InvalidLoginException(errorResponse?.message))
                                ErrorConstants.NOT_AUTHORIZED_403 ->
                                    Single.error(AccountNotActiveException(errorResponse?.message))
                                else -> Single.error(it)
                            }
                        }
                        is NotAuthenticatedException -> {
                            val message =
                                if (!it.message.isNullOrBlank()) it.message
                                else if (it.cause.isNotNull() && !it.cause?.message.isNullOrBlank()) it.cause?.message
                                else "No active user with those credentials"
                            Single.error(
                                NotAuthorizedException(
                                    message ?: ""
                                )
                            )

                        }
                        else -> {
                            Single.error(it)
                        }
                    }
                }
        )


    override fun getCurrentUser(): Single<UserModel> {
        TODO("Not yet implemented")
    }

    override fun setCurrentUser(user: UserModel): Completable {
        TODO("Not yet implemented")
    }

    override fun logout(): Single<Any> =
        callSingle(authApi.logout())
            .flatMap {
                Single.just(true)
                    .onErrorResumeNext {
                        when (it) {
                            is HttpException -> {
                                val code = it.code()
                                val errorResponse =
                                    it.toErrorResponseModel<BaseErrorResponse<Any>>()
                                when (code) {
                                    ErrorConstants.INPUT_VALIDATION_400,
                                    ErrorConstants.NOT_FOUND_404 ->
                                        Single.error(InvalidLoginException(errorResponse?.message))
                                    ErrorConstants.NOT_AUTHORIZED_403 ->
                                        Single.error(AccountNotActiveException(errorResponse?.message))
                                    else -> Single.error(it)
                                }
                            }
                            is NotAuthenticatedException -> {
                                val message =
                                    if (!it.message.isNullOrBlank()) it.message
                                    else if (it.cause.isNotNull() && !it.cause?.message.isNullOrBlank()) it.cause?.message
                                    else "No active user with those credentials"
                                Single.error(
                                    NotAuthorizedException(
                                        message ?: ""
                                    )
                                )

                            }
                            else -> {
                                Single.error(it)
                            }
                        }
                    }
            }


    override fun refreshAccessToken(token: String): Completable {
        TODO("Not yet implemented")
    }

    override fun getTaxObject(userId : String): Single<TaxInformationResponse> =
        callSingle(authApi.getTaxObject(userId).flatMap {
           if(it.data != null) {
               Single.just(it.data!!)
           }else {
               val message = "No active user with those tax credentials"
               Single.error(
                   NotAuthorizedException(
                       message
                   )
               )
           }

        }.onErrorResumeNext {
            when (it) {
                is HttpException -> {
                    val code = it.code()
                    val errorResponse = it.toErrorResponseModel<BaseErrorResponse<Any>>()
                    when (code) {
                        ErrorConstants.INPUT_VALIDATION_400,
                        ErrorConstants.NOT_FOUND_404 ->
                            Single.error(InvalidLoginException(errorResponse?.message))
                        ErrorConstants.NOT_AUTHORIZED_403 ->
                            Single.error(AccountNotActiveException(errorResponse?.message))
                        else -> Single.error(it)
                    }
                }
                is NotAuthenticatedException -> {
                    val message =
                        if (!it.message.isNullOrBlank()) it.message
                        else if (it.cause.isNotNull() && !it.cause?.message.isNullOrBlank()) it.cause?.message
                        else "No active user with those credentials"
                    Single.error(
                        NotAuthorizedException(
                            message ?: ""
                        )
                    )

                }
                else -> {
                    Single.error(it)
                }
            }
        }
        )


}