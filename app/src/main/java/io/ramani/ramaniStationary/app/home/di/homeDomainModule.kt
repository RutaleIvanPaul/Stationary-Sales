package io.ramani.ramaniStationary.app.home.di

import io.ramani.ramaniStationary.data.home.models.request.DailySalesStatsRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetMerchantRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetProductRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetTaxRequestModel
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.ramani.ramaniStationary.domain.home.useCase.DailySalesStatsUseCase
import io.ramani.ramaniStationary.domain.home.useCase.GetMerchantsUseCase
import io.ramani.ramaniStationary.domain.home.useCase.GetProductsUseCase
import io.ramani.ramaniStationary.domain.home.useCase.GetTaxesUseCase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val homeDomainModule = Kodein.Module("homeDomainModule") {

    bind<BaseSingleUseCase<List<DailySalesStatsModel>, DailySalesStatsRequestModel>>("DailySalesStatsUseCase") with provider {
        DailySalesStatsUseCase(instance(), instance(), instance("homeDataSource"))
    }

    bind<BaseSingleUseCase<List<TaxModel>, GetTaxRequestModel>>("GetTaxesUseCase") with provider {
        GetTaxesUseCase(instance(), instance(), instance("homeDataSource"))
    }

    bind<BaseSingleUseCase<List<ProductModel>, GetProductRequestModel>>("GetProductsUseCase") with provider {
        GetProductsUseCase(instance(), instance(), instance("homeDataSource"))
    }

    bind<BaseSingleUseCase<List<MerchantModel>, GetMerchantRequestModel>>("GetMerchantsUseCase") with provider {
        GetMerchantsUseCase(instance(), instance(), instance("homeDataSource"))
    }

}