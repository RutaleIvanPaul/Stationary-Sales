package io.ramani.ramaniStationary.app.credit.di

import io.ramani.ramaniStationary.data.credit.models.request.GetLocationsRequestModel
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.credit.model.LocationModel
import io.ramani.ramaniStationary.domain.credit.useCase.GetListLocationsUseCase
import io.ramani.ramaniStationary.domain.entities.PagedList
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val creditDomainModule = Kodein.Module("creditDomainModule") {

    bind<BaseSingleUseCase<PagedList<LocationModel>, GetLocationsRequestModel>>("GetListLocationsUseCase") with provider {
        GetListLocationsUseCase(instance(), instance(), instance("creditDataSource"))
    }

}