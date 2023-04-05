package io.ramani.ramaniStationary.data.createmerchant

import io.ramani.ramaniStationary.data.createmerchant.models.response.TopPerformersRemoteModel
import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.reactivex.Single
import retrofit2.http.*

interface CreateMerchantApi {

    @GET("/api/v1/top-performers")
    fun getTopPerformers(
        @Query("companyId") companyId: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("size") size: Int
    ): Single<BaseResponse<TopPerformersRemoteModel>>

}