package io.ramani.ramaniStationary.data.createmerchant

import io.ramani.ramaniStationary.data.createmerchant.models.request.RegisterMerchantRequestModel
import io.ramani.ramaniStationary.data.createmerchant.models.response.TopPerformersRemoteModel
import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.ramani.ramaniStationary.data.home.models.response.MerchantRemoteModel
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

    @POST("/register/merchant/04-04-2023")
    fun registerMerchant(
        @Body merchant: RegisterMerchantRequestModel
    ): Single<BaseResponse<MerchantRemoteModel>>

}