package io.ramani.ramaniStationary.data.reports

import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.ramani.ramaniStationary.data.reports.models.response.SalesSummaryStatisticsRemoteModel
import io.reactivex.Single
import retrofit2.http.*

interface ReportsApi {

    @GET("/api/v1/sales-summary-statistics")
    fun getSalesSummaryStatistics(
        @Query("companyId") companyId: String,
        @Query("salesPersonUID") salesPersonUID: String,
        @Query("page") page: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<BaseResponse<SalesSummaryStatisticsRemoteModel>>

}