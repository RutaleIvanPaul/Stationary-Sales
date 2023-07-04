package io.ramani.ramaniStationary.app.reports.di

import io.ramani.ramaniStationary.data.reports.models.request.GetSalesSummaryStatisticsRequestModel
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.reports.model.SalesSummaryStatisticsModel
import io.ramani.ramaniStationary.domain.reports.useCase.GetSalesSummaryStatisticsUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val reportsDomainModule = Kodein.Module("reportsDomainModule") {

    bind<BaseSingleUseCase<SalesSummaryStatisticsModel, GetSalesSummaryStatisticsRequestModel>>("GetSalesSummaryStatisticsUseCase") with provider {
        GetSalesSummaryStatisticsUseCase(instance(), instance(), instance("reportsDataSource"))
    }

}