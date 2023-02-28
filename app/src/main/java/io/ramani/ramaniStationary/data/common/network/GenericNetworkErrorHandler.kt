package io.ramani.ramaniStationary.data.common.network

import io.ramani.ramaniStationary.domain.entities.BaseErrorResponse
import io.ramani.ramaniStationary.domain.entities.exceptions.ConnectionTimeoutException
import io.ramani.ramaniStationary.domain.entities.exceptions.NoInternetConnectionException
import io.ramani.ramaniStationary.domain.entities.exceptions.TokenExpiredException
import io.ramani.ramaniStationary.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniStationary.domainCore.exceptions.PermissionsChangedException
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by Amr on 10/13/17.
 */
class GenericNetworkErrorHandler(private val stringProvider: IStringProvider) :
    NetworkErrorHandler {
    override fun handleError(throwable: Throwable): Throwable =
        when (throwable) {
            is UnknownHostException,
            is ConnectException -> handleNoConnectionException()
            is SocketTimeoutException,
            is SocketException -> handleConnectionTimeoutException()
            is HttpException -> handleHttpException(throwable)
            else -> throwable
        }

    private fun handleNoConnectionException(): Throwable =
        NoInternetConnectionException(stringProvider.getConnectionErrorMessage())

    private fun handleConnectionTimeoutException(): Throwable =
        ConnectionTimeoutException(stringProvider.getConnectionErrorMessage())

    private fun handleHttpException(exception: HttpException): Throwable {
        val code = exception.code()
        return when (code) {
            ErrorConstants.NOT_AUTHENTICATED_401,
            ErrorConstants.OLD_TOKEN_ALREADY_REFRESHED_406 -> {
                val errorResponse = exception.toErrorResponseModel<BaseErrorResponse<Any>>()
                NotAuthenticatedException(
                    errorResponse?.message
                        ?: ""
                )
            }
            ErrorConstants.REQUEST_TIMEOUT_408 -> {
                val errorResponse = exception.toErrorResponseModel<BaseErrorResponse<Any>>()
                TokenExpiredException(
                    errorResponse?.message
                        ?: ""
                )
            }
            ErrorConstants.GONE_410 -> {
                val errorResponse = exception.toErrorResponseModel<BaseErrorResponse<Any>>()
                PermissionsChangedException(
                    errorResponse?.message
                        ?: ""
                )
            }
            else -> exception
        }
    }
}