package io.ramani.ramaniStationary.app.home.di

import io.ramani.ramaniStationary.data.home.models.request.*
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.model.*
import io.ramani.ramaniStationary.domain.home.useCase.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val homeDomainModule = Kodein.Module("homeDomainModule") {

    bind<BaseSingleUseCase<PagedList<DailySalesStatsModel>, DailySalesStatsRequestModel>>("DailySalesStatsUseCase") with provider {
        DailySalesStatsUseCase(instance(), instance(), instance("homeDataSource"))
    }

    bind<BaseSingleUseCase<PagedList<TaxModel>, GetTaxRequestModel>>("GetTaxesUseCase") with provider {
        GetTaxesUseCase(instance(), instance(), instance("homeDataSource"))
    }

    bind<BaseSingleUseCase<PagedList<ProductModel>, GetProductRequestModel>>("GetProductsUseCase") with provider {
        GetProductsUseCase(instance(), instance(), instance("homeDataSource"))
    }

    bind<BaseSingleUseCase<PagedList<MerchantModel>, GetMerchantRequestModel>>("GetMerchantsUseCase") with provider {
        GetMerchantsUseCase(instance(), instance(), instance("homeDataSource"))
    }

    bind<BaseSingleUseCase<TaxInformationModel, GetTaxInformationRequestModel>>("GetTaxInformationUseCase") with provider {
        GetTaxInformationUseCase(instance(), instance(), instance("homeDataSource"))
    }

}