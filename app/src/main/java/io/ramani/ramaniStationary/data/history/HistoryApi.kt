package io.ramani.ramaniStationary.data.history

import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.ramani.ramaniStationary.data.history.models.response.HistoryResponse
import io.ramani.ramaniStationary.data.history.models.response.OrderDetailsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoryApi {

    @GET("/sfa/get/printable/receipt/zreport/range/v2")
    fun getZreportByRange(
        @Query("uin")uin: String, @Query("companyId")companyId: String,
        @Query("startDate")startDate: String,
        @Query("endDate")endDate: String,
        @Query("sellerName")sellerName: String): Single<BaseResponse<String>>

    @GET("/sfa/get/printable/receipt/zreport/v2")
    fun getXReport(
        @Query("uin")uin: String,
        @Query("date")date: String,
        @Query("sellerName")sellerName: String,
        @Query("companyId")companyId:String
    ): Single<BaseResponse<String>>


    @GET("/orders/details/V2")
    fun getOrderDetails(
        @Query("orderId") orderId: String
    ): Single<BaseResponse<List<OrderDetailsResponse>>>

    @GET("/api/v1/salesActivity-summary/{userId}")
    fun getHistory(
        @Path("userId")userId: String,
        @Query("day")day: Int,
        @Query("month")month: String,
        @Query("year")year: Int
    ): Single<BaseResponse<HistoryResponse>>

}