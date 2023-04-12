package io.ramani.ramaniStationary.app.history.di

import io.ramani.ramaniStationary.data.history.models.request.*
import io.ramani.ramaniStationary.data.history.models.response.HistoryResponse
import io.ramani.ramaniStationary.data.history.models.response.OrderDetailsResponse
import io.ramani.ramaniStationary.data.history.models.response.TRAReceipt
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.history.useCase.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val historyDomainModule = Kodein.Module("historyDomainModule"){
    bind<BaseSingleUseCase<HistoryResponse?, GetHistoryRequestModel>>("GetHistoryUseCase") with provider {
        GetHistoryUseCase(instance(),instance(),instance("historyRemoteDataSource"))
    }

    bind<BaseSingleUseCase<String?, GetZReportRequestModel>>("GetZreportByRangeUseCase") with provider {
        GetZreportByRangeUseCase(instance(),instance(),instance("historyRemoteDataSource"))
    }

    bind<BaseSingleUseCase<String?, GetXReportRequestModel>>("GetXReportUseCase") with provider {
        GetXReportUseCase(instance(),instance(),instance("historyRemoteDataSource"))
    }

    bind<BaseSingleUseCase<List<OrderDetailsResponse>?, GetOrderDetailsRequestModel>>("GetOrderDetailsUseCase") with provider {
        GetOrderDetailsUseCase(instance(),instance(),instance("historyRemoteDataSource"))
    }

    bind<BaseSingleUseCase<TRAReceipt, PrintReceiptRequest>>("GetReceiptUseCase") with provider {
        GetReceiptUseCase(instance(),instance(),instance("historyRemoteDataSource"))
    }
}