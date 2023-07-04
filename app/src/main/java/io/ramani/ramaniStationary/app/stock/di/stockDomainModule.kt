package io.ramani.ramaniStationary.app.stock.di

import io.ramani.ramaniStationary.data.stock.models.request.AvailableStockRequestModel
import io.ramani.ramaniStationary.data.stock.models.response.GetRollingStock
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.stock.useCase.AvailableStockUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val stockDomainModule = Kodein.Module("stockDomainModule"){

    bind<BaseSingleUseCase<GetRollingStock?, AvailableStockRequestModel>>("AvailableStockUseCase") with provider {
        AvailableStockUseCase(instance(),instance(),instance("stockRepository"))
    }
}