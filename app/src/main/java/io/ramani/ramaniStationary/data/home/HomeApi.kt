package io.ramani.ramaniStationary.data.home

import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.ramani.ramaniStationary.data.home.models.DailySalesStatsRemoteModel
import io.reactivex.Single
import retrofit2.http.*

interface HomeApi {
    @POST("/sales-activity/daily-sales-stats/{date}")
    fun getDailyStats(
        @Path("date") date: String,
        @Query("companyId") companyId: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Single<BaseResponse<List<DailySalesStatsRemoteModel>>>

}