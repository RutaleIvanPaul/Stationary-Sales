package io.ramani.ramaniStationary.app.createorder.di

import io.ramani.ramaniStationary.data.common.network.ServiceHelper
import io.ramani.ramaniStationary.data.createorder.mappers.AvailableProductRemoteMapper
import io.ramani.ramaniStationary.data.createorder.mappers.AvailableStockRemoteMapper
import io.ramani.ramaniStationary.data.createorder.mappers.SaleRemoteMapper
import io.ramani.ramaniStationary.data.createorder.models.CreateOrderApi
import io.ramani.ramaniStationary.data.createorder.models.CreateOrderLocalDataSource
import io.ramani.ramaniStationary.data.createorder.models.CreateOrderRemoteDataSource
import io.ramani.ramaniStationary.data.createorder.models.CreateOrderRepository
import io.ramani.ramaniStationary.data.createorder.models.response.AvailableProductRemoteModel
import io.ramani.ramaniStationary.data.createorder.models.response.AvailableStockRemoteModel
import io.ramani.ramaniStationary.data.createorder.models.response.SaleRemoteModel
import io.ramani.ramaniStationary.data.home.HomeApi
import io.ramani.ramaniStationary.data.home.HomeLocalDataSource
import io.ramani.ramaniStationary.data.home.HomeRemoteDataSource
import io.ramani.ramaniStationary.data.home.HomeRepository
import io.ramani.ramaniStationary.data.home.mappers.DailySalesStatsRemoteMapper
import io.ramani.ramaniStationary.data.home.models.response.DailySalesStatsRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.createorder.CreateOrderDataSource
import io.ramani.ramaniStationary.domain.createorder.model.AvailableProductModel
import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel
import io.ramani.ramaniStationary.domain.createorder.model.SaleModel
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val createOrderDataModule = Kodein.Module("createOrderDataModule") {

    bind<CreateOrderApi>() with provider {
        ServiceHelper.createService<CreateOrderApi>(instance())
    }

    bind<CreateOrderDataSource>("createOrderDataSource") with singleton {
        CreateOrderRepository(
            instance("remoteCreateOrderDataSource"),
            instance("localCreateOrderDataSource")
        )
    }

    bind<CreateOrderDataSource>("remoteCreateOrderDataSource") with singleton {
        CreateOrderRemoteDataSource(
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bind<CreateOrderDataSource>("localCreateOrderDataSource") with singleton {
        CreateOrderLocalDataSource(
            instance(),
            instance()
        )
    }

    // data mapper
    bind<ModelMapper<AvailableStockRemoteModel, AvailableStockModel>>() with provider {
        AvailableStockRemoteMapper(
            instance()
        )
    }

    bind<ModelMapper<AvailableProductRemoteModel, AvailableProductModel>>() with provider {
        AvailableProductRemoteMapper()
    }

    bind<ModelMapper<SaleRemoteModel, SaleModel>>() with provider {
        SaleRemoteMapper()
    }

}