package io.ramani.ramaniStationary.app.createorder.di

import io.ramani.ramaniStationary.data.createorder.models.request.GetAvailableStockRequestModel
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel
import io.ramani.ramaniStationary.domain.createorder.useCase.GetAvailableStockUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val createOrderDomainModule = Kodein.Module("createOrderDomainModule") {

    bind<BaseSingleUseCase<List<AvailableStockModel>, GetAvailableStockRequestModel>>("GetAvailableStockUseCase") with provider {
        GetAvailableStockUseCase(instance(), instance(), instance("createOrderDataSource"))
    }

}