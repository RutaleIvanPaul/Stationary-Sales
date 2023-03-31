package io.ramani.ramaniStationary.app.stock.di

import io.ramani.ramaniStationary.data.common.network.ServiceHelper
import io.ramani.ramaniStationary.data.home.HomeApi
import io.ramani.ramaniStationary.data.stock.StockApi
import io.ramani.ramaniStationary.data.stock.StockRemoteDataSource
import io.ramani.ramaniStationary.data.stock.StockRepository
import io.ramani.ramaniStationary.domain.stock.StockDataSource
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val stockDataModule = Kodein.Module("stockDataModule"){
    bind<StockDataSource>("stockRepository") with singleton {
        StockRepository(instance("stockRemoteDataSource"))
    }

    bind<StockDataSource>("stockRemoteDataSource") with singleton {
        StockRemoteDataSource(instance("stockApi"))
    }

    bind<StockApi>("stockApi") with provider {
        ServiceHelper.createService<StockApi>(instance())
    }
}