package io.ramani.ramaniStationary.app.reports.di

import io.ramani.ramaniStationary.data.common.network.ServiceHelper
import io.ramani.ramaniStationary.data.reports.ReportsApi
import io.ramani.ramaniStationary.data.reports.ReportsRemoteDataSource
import io.ramani.ramaniStationary.data.reports.ReportsRepository
import io.ramani.ramaniStationary.data.reports.mappers.SalesSummaryStatisticsRemoteMapper
import io.ramani.ramaniStationary.data.reports.mappers.ValuePercentageRemoteMapper
import io.ramani.ramaniStationary.data.reports.models.response.SalesSummaryStatisticsRemoteModel
import io.ramani.ramaniStationary.data.reports.models.response.ValuePercentageRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.reports.ReportsDataSource
import io.ramani.ramaniStationary.domain.reports.model.SalesSummaryStatisticsModel
import io.ramani.ramaniStationary.domain.reports.model.ValuePercentageModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val reportsDataModule = Kodein.Module("reportsDataModule") {
    bind<ReportsApi>() with provider {
        ServiceHelper.createService<ReportsApi>(instance())
    }

    bind<ReportsDataSource>("reportsDataSource") with singleton {
        ReportsRepository(
            instance("remoteReportsDataSource"),
            instance("localReportsDataSource")
        )
    }

    bind<ReportsDataSource>("remoteReportsDataSource") with singleton {
        ReportsRemoteDataSource(
            instance(),
            instance()
        )
    }

    bind<ReportsDataSource>("localReportsDataSource") with singleton {
        ReportsRemoteDataSource(
            instance(),
            instance()
        )
    }

    // data mapper
    bind<ModelMapper<ValuePercentageRemoteModel, ValuePercentageModel>>() with provider {
        ValuePercentageRemoteMapper()
    }

    bind<ModelMapper<SalesSummaryStatisticsRemoteModel, SalesSummaryStatisticsModel>>() with provider {
        SalesSummaryStatisticsRemoteMapper(
            instance()
        )
    }

}