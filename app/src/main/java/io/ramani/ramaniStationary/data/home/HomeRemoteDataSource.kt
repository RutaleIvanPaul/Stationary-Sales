package io.ramani.ramaniStationary.data.home

import io.ramani.ramaniStationary.data.common.network.ErrorConstants
import io.ramani.ramaniStationary.data.common.network.toErrorResponseModel
import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.home.models.response.DailySalesStatsRemoteModel
import io.ramani.ramaniStationary.data.home.models.response.MerchantRemoteModel
import io.ramani.ramaniStationary.data.home.models.response.ProductRemoteModel
import io.ramani.ramaniStationary.data.home.models.response.TaxRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.base.mappers.mapFromWith
import io.ramani.ramaniStationary.domain.entities.BaseErrorResponse
import io.ramani.ramaniStationary.domain.entities.exceptions.AccountNotActiveException
import io.ramani.ramaniStationary.domain.entities.exceptions.InvalidLoginException
import io.ramani.ramaniStationary.domain.entities.exceptions.NotAuthorizedException
import io.ramani.ramaniStationary.domain.entities.exceptions.ParseResponseException
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.ramani.ramaniStationary.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniStationary.domainCore.lang.isNotNull
import io.reactivex.Single
import retrofit2.HttpException

class HomeRemoteDataSource(
    private val homeApi: HomeApi,
    private val dailySalesStatsRemoteMapper: ModelMapper<DailySalesStatsRemoteModel, DailySalesStatsModel>,
    private val taxRemoteMapper: ModelMapper<TaxRemoteModel, TaxModel>,
    private val productRemoteMapper: ModelMapper<ProductRemoteModel, ProductModel>,
    private val merchantRemoteMapper: ModelMapper<MerchantRemoteModel, MerchantModel>,
    ) : HomeDataSource, BaseRemoteDataSource() {

    override fun getDailySalesStats(companyId: String, page: Int, size: Int, startDate: String, endDate: String): Single<List<DailySalesStatsModel>> =
        callSingle(
            homeApi.getDailyStats(companyId, page, size, startDate, endDate).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(dailySalesStatsRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
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

    override fun getTaxes(companyId: String, userId: String, date: String, page: Int, size: Int): Single<List<TaxModel>> =
        callSingle(
            homeApi.getTaxes(date, companyId, userId, page, size).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(taxRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
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

    override fun getProducts(date: String, archived: Boolean, page: Int, size: Int ): Single<List<ProductModel>> =
        callSingle(
            homeApi.getProducts(date, archived, page, size).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(productRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
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

    override fun getMerchants(date: String, isActive: Boolean, page: Int, size: Int ): Single<List<MerchantModel>> =
        callSingle(
            homeApi.getMerchants(date, isActive, page, size).flatMap {
                val data = it.data
                if (data != null) {
                    Single.just(data.mapFromWith(merchantRemoteMapper))
                } else {
                    Single.error(ParseResponseException())
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
}