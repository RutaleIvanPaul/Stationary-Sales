package io.ramani.ramaniStationary.data.home

import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.ramani.ramaniStationary.data.home.models.response.DailySalesStatsRemoteModel
import io.ramani.ramaniStationary.data.home.models.response.MerchantRemoteModel
import io.ramani.ramaniStationary.data.home.models.response.ProductRemoteModel
import io.ramani.ramaniStationary.data.home.models.response.TaxRemoteModel
import io.reactivex.Single
import retrofit2.http.*

interface HomeApi {
    @POST("/sales-activity/daily-sales-stats/16-02-2023")
    fun getDailyStats(
        @Query("companyId") companyId: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<BaseResponse<List<DailySalesStatsRemoteModel>>>

    @POST("/tax/16-02-2022")
    fun getTaxes(
        @Query("companyId") companyId: String,
        @Query("userId") userId: String,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<BaseResponse<List<TaxRemoteModel>>>

    @POST("/accounts/{id}/products/16-02-2023")
    fun getProducts(
        @Path("id") companyId: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("isArchived") archived: Boolean,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<BaseResponse<List<ProductRemoteModel>>>

    @POST("/merchants/16-02-2023")
    fun getMerchants(
        @Query("date") date: String,
        @Query("isActive") isActive: Boolean,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<BaseResponse<List<MerchantRemoteModel>>>

}