package io.ramani.ramaniStationary.data.createorder.models

import io.ramani.ramaniStationary.data.createorder.models.response.AvailableStockRemoteModel
import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.reactivex.Single
import retrofit2.http.*

interface CreateOrderApi {

    @GET("/sfa/stock/available")
    fun getAvailableStock(
        @Header("invalidate_cache") invalidate_cache: String,
        @Query("salesPersonUID") salesPersonUID: String
    ): Single<BaseResponse<List<AvailableStockRemoteModel>>>

}