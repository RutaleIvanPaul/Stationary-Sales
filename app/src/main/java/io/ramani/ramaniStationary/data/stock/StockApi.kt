package io.ramani.ramaniStationary.data.stock

import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.ramani.ramaniStationary.data.stock.models.response.GetRollingStock
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("/sfa/stock/available")
    fun getAvailableStock(
        @Query("salesPersonUID") salesPersonUID: String
    ): Single<BaseResponse<List<GetRollingStock>>>

}