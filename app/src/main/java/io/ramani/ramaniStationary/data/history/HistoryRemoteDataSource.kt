package io.ramani.ramaniStationary.data.history

import io.ramani.ramaniStationary.data.common.network.ErrorConstants
import io.ramani.ramaniStationary.data.common.network.toErrorResponseModel
import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.ramani.ramaniStationary.data.history.models.response.HistoryResponse
import io.ramani.ramaniStationary.data.history.models.response.OrderDetailsResponse
import io.ramani.ramaniStationary.data.history.models.response.TRAReceipt
import io.ramani.ramaniStationary.domain.base.exceptions.ItemNotFoundException
import io.ramani.ramaniStationary.domain.entities.BaseErrorResponse
import io.ramani.ramaniStationary.domain.entities.exceptions.AccountNotActiveException
import io.ramani.ramaniStationary.domain.entities.exceptions.InvalidLoginException
import io.ramani.ramaniStationary.domain.entities.exceptions.NotAuthorizedException
import io.ramani.ramaniStationary.domain.history.HistoryDataSource
import io.ramani.ramaniStationary.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniStationary.domainCore.lang.isNotNull
import io.reactivex.Single
import retrofit2.HttpException

class HistoryRemoteDataSource(
    private val historyApi: HistoryApi
): BaseRemoteDataSource(),HistoryDataSource {
    override fun getZreportByRange(
        uin: String,
        companyId: String,
        startDate: String,
        endDate: String,
        sellerName: String
    ): Single<String?> = callSingle(
            historyApi.getZreportByRange(uin,
                companyId,
                startDate,
                endDate,
                sellerName).flatMap {
                if(it.data != null) {
                    Single.just(it.data!!)
                }else {
                    val message = "Could not get Z Report"
                    Single.error(
                        ItemNotFoundException(
                            message
                        )
                    )
                }
            }.onErrorResumeNext {
                if (it is HttpException) {
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
                } else if (it is NotAuthenticatedException) {
                    val message =
                        if (!it.message.isNullOrBlank()) it.message
                        else if (it.cause.isNotNull() && !it.cause?.message.isNullOrBlank()) it.cause?.message
                        else "No active user with those credentials"
                    Single.error(
                        NotAuthorizedException(
                            message ?: ""
                        )
                    )

                } else {
                    Single.error(it)
                }
            }
        )


    override fun getXReport(
        uin: String,
        date: String,
        sellerName: String,
        companyId: String
    ): Single<String?> = callSingle(
        historyApi.getXReport(
            uin,
            date,
            sellerName,
            companyId
        ).flatMap {
            if(it.data != null) {
                Single.just(it.data!!)
            }else {
                val message = "Could not get X Report"
                Single.error(
                    ItemNotFoundException(
                        message
                    )
                )
            }
        }.onErrorResumeNext {
            if (it is HttpException) {
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
            } else if (it is NotAuthenticatedException) {
                val message =
                    if (!it.message.isNullOrBlank()) it.message
                    else if (it.cause.isNotNull() && !it.cause?.message.isNullOrBlank()) it.cause?.message
                    else "No active user with those credentials"
                Single.error(
                    NotAuthorizedException(
                        message ?: ""
                    )
                )

            } else {
                Single.error(it)
            }
        }
    )

    override fun getOrderDetails(orderId: String): Single<List<OrderDetailsResponse>?>  = callSingle(
        historyApi.getOrderDetails(orderId).flatMap {
            Single.just(it.data)
        }.onErrorResumeNext {
            if (it is HttpException) {
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
            } else if (it is NotAuthenticatedException) {
                val message =
                    if (!it.message.isNullOrBlank()) it.message
                    else if (it.cause.isNotNull() && !it.cause?.message.isNullOrBlank()) it.cause?.message
                    else "No active user with those credentials"
                Single.error(
                    NotAuthorizedException(
                        message ?: ""
                    )
                )

            } else {
                Single.error(it)
            }
        }
    )

    override fun getHistory(
        userId: String,
        day: Int,
        month: String,
        year: Int
    ): Single<HistoryResponse?> = callSingle(
        historyApi.getHistory(userId,day,month,year).flatMap {
            Single.just(it.data)
        }
    )

    override fun getReceipt(id: String, uin: String, sellerName: String): Single<TRAReceipt>  = callSingle(
        historyApi.getPrintableReceipt(id, uin, sellerName).flatMap {
            Single.just(it.data)
        }
    )
}