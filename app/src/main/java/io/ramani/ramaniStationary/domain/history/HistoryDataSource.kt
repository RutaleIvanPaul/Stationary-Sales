package io.ramani.ramaniStationary.domain.history

import io.ramani.ramaniStationary.data.entities.BaseResponse
import io.ramani.ramaniStationary.data.history.models.response.HistoryResponse
import io.ramani.ramaniStationary.data.history.models.response.OrderDetailsResponse
import io.reactivex.Single

interface HistoryDataSource {
    fun getZreportByRange(uin: String,
                          companyId: String,
                          startDate: String,
                          endDate: String,
                          sellerName: String): Single<String?>

    fun getXReport(
        uin: String,
        date: String,
        sellerName: String,
        companyId:String
    ): Single<String?>


    fun getOrderDetails(
        orderId: String
    ): Single<List<OrderDetailsResponse>?>

    fun getHistory(
        userId: String,
        day: Int,
        month: String,
        year: Int
    ): Single<HistoryResponse?>
}