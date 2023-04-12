package io.ramani.ramaniStationary.domain.history.useCase

import io.ramani.ramaniStationary.data.history.models.request.*
import io.ramani.ramaniStationary.data.history.models.response.HistoryResponse
import io.ramani.ramaniStationary.data.history.models.response.OrderDetailsResponse
import io.ramani.ramaniStationary.data.history.models.response.TRAReceipt
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.history.HistoryDataSource
import io.reactivex.Single

class GetHistoryUseCase
    (
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val historyDataSource: HistoryDataSource
) : BaseSingleUseCase<HistoryResponse?, GetHistoryRequestModel>(
    threadExecutor, postThreadExecutor
) {
    override fun buildUseCaseSingle(params: GetHistoryRequestModel?): Single<HistoryResponse?> =
        historyDataSource.getHistory(
            params!!.userId, params.day, params.month, params.year
        )

}

class GetZreportByRangeUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val historyDataSource: HistoryDataSource
) : BaseSingleUseCase<String?, GetZReportRequestModel>(threadExecutor, postThreadExecutor) {
    override fun buildUseCaseSingle(params: GetZReportRequestModel?): Single<String?> =
        historyDataSource.getZreportByRange(
            params!!.uin, params.companyId, params.startDate, params.endDate, params.sellerName
        )

}

class GetXReportUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val historyDataSource: HistoryDataSource
) : BaseSingleUseCase<String?, GetXReportRequestModel>(threadExecutor, postThreadExecutor) {
    override fun buildUseCaseSingle(params: GetXReportRequestModel?): Single<String?> =
        historyDataSource.getXReport(
            params!!.uin, params.date, params.sellerName, params.companyId
        )

}

class GetOrderDetailsUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val historyDataSource: HistoryDataSource
) : BaseSingleUseCase<List<OrderDetailsResponse>?, GetOrderDetailsRequestModel>(
    threadExecutor, postThreadExecutor
) {
    override fun buildUseCaseSingle(params: GetOrderDetailsRequestModel?): Single<List<OrderDetailsResponse>?> =
        historyDataSource.getOrderDetails(params!!.orderId)

}

class GetReceiptUseCase(
    threadExecutor: ThreadExecutor,
    postThreadExecutor: PostThreadExecutor,
    private val historyDataSource: HistoryDataSource
) : BaseSingleUseCase<TRAReceipt, PrintReceiptRequest>(
    threadExecutor, postThreadExecutor
) {
    override fun buildUseCaseSingle(params: PrintReceiptRequest?): Single<TRAReceipt> =
        historyDataSource.getReceipt(params!!.id, params.uin, params.sellerName)


}


