package io.ramani.ramaniStationary.data.createorder.models

import io.ramani.ramaniStationary.data.common.network.ErrorConstants
import io.ramani.ramaniStationary.data.common.network.toErrorResponseModel
import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.createorder.models.response.AvailableStockRemoteModel
import io.ramani.ramaniStationary.data.entities.PaginationMetaRemote
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.base.mappers.mapFromWith
import io.ramani.ramaniStationary.domain.createorder.CreateOrderDataSource
import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel
import io.ramani.ramaniStationary.domain.entities.BaseErrorResponse
import io.ramani.ramaniStationary.domain.entities.PaginationMeta
import io.ramani.ramaniStationary.domain.entities.exceptions.AccountNotActiveException
import io.ramani.ramaniStationary.domain.entities.exceptions.InvalidLoginException
import io.ramani.ramaniStationary.domain.entities.exceptions.NotAuthorizedException
import io.ramani.ramaniStationary.domain.entities.exceptions.ParseResponseException
import io.ramani.ramaniStationary.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniStationary.domainCore.lang.isNotNull
import io.reactivex.Single
import retrofit2.HttpException

class CreateOrderRemoteDataSource(
    private val createOrderApi: CreateOrderApi,
    private val availableStockRemoteMapper: ModelMapper<AvailableStockRemoteModel, AvailableStockModel>,
    private val metaRemoteMapper: ModelMapper<PaginationMetaRemote, PaginationMeta>,
) : CreateOrderDataSource, BaseRemoteDataSource() {

    override fun getAvailableStock(salesPersonUID: String): Single<List<AvailableStockModel>> =
        callSingle(
            createOrderApi.getAvailableStock("true", salesPersonUID).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(availableStockRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
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