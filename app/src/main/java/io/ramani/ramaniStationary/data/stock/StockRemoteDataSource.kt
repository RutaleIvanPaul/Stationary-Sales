package io.ramani.ramaniStationary.data.stock

import io.ramani.ramaniStationary.data.common.network.ErrorConstants
import io.ramani.ramaniStationary.data.common.network.toErrorResponseModel
import io.ramani.ramaniStationary.data.common.source.remote.BaseRemoteDataSource
import io.ramani.ramaniStationary.data.stock.models.response.GetRollingStock
import io.ramani.ramaniStationary.domain.entities.BaseErrorResponse
import io.ramani.ramaniStationary.domain.entities.exceptions.AccountNotActiveException
import io.ramani.ramaniStationary.domain.entities.exceptions.InvalidLoginException
import io.ramani.ramaniStationary.domain.entities.exceptions.NotAuthorizedException
import io.ramani.ramaniStationary.domain.stock.StockDataSource
import io.ramani.ramaniStationary.domainCore.exceptions.NotAuthenticatedException
import io.ramani.ramaniStationary.domainCore.lang.isNotNull
import io.reactivex.Single
import retrofit2.HttpException

class StockRemoteDataSource(
    private val stockApi: StockApi
):BaseRemoteDataSource(),StockDataSource {
    override fun getAvailableStock(salesPersonUID: String): Single<GetRollingStock?> =
        callSingle(
            stockApi.getAvailableStock(salesPersonUID).flatMap {
                val data = it?.data?.get(0)
                Single.just(data)
            }
        )
}