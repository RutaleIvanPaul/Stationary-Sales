package io.ramani.ramaniStationary.data.home

import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.ramani.ramaniStationary.data.home.models.response.*
import io.reactivex.Single
import retrofit2.http.*

interface HomeApi {
    @GET("/sales-activity/daily-sales-stats/16-02-2023")
    fun getDailyStats(
        @Query("companyId") companyId: String,
        @Query("salesPersonUID") salesPersonUID: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<BaseResponse<List<DailySalesStatsRemoteModel>>>

    @GET("/tax/16-02-2022")
    fun getTaxes(
        @Query("companyId") companyId: String,
        @Query("userId") userId: String,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<BaseResponse<List<TaxRemoteModel>>>

    @GET("/accounts/{id}/products/16-02-2023")
    fun getProducts(
        @Path("id") companyId: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("isArchived") archived: Boolean,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<BaseResponse<List<ProductRemoteModel>>>

    @GET("/merchants/16-02-2023")
    fun getMerchants(
        @Query("companyId") companyId: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("isActive") isActive: Boolean,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Single<BaseResponse<List<MerchantRemoteModel>>>

    @GET("/get/tax/object/associated/with/user")
    fun getTaxInformationByUserId(
        @Query("userId") userId: String
    ): Single<BaseResponse<TaxInformationRemoteModel>>

    @GET("/get/accounts/company/details/v2")
    fun getAccountDetails(
        @Query("companyId") companyId: String
    ): Single<BaseResponse<List<UserAccountDetailsRemoteModel>>>
}