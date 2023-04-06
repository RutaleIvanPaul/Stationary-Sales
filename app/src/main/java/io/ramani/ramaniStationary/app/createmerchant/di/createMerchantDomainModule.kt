package io.ramani.ramaniStationary.app.createmerchant

import io.ramani.ramaniStationary.data.createmerchant.models.request.GetTopPerformersRequestModel
import io.ramani.ramaniStationary.data.createmerchant.models.request.RegisterMerchantRequestModel
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.ramani.ramaniStationary.domain.createmerchant.useCase.GetTopPerformersUseCase
import io.ramani.ramaniStationary.domain.createmerchant.useCase.RegisterMerchantUseCase
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val createMerchantDomainModule = Kodein.Module("createMerchantDomainModule") {

    bind<BaseSingleUseCase<TopPerformersModel, GetTopPerformersRequestModel>>("GetTopPerformersUseCase") with provider {
        GetTopPerformersUseCase(instance(), instance(), instance("createMerchantDataSource"))
    }

    bind<BaseSingleUseCase<MerchantModel, RegisterMerchantRequestModel>>("RegisterMerchantUseCase") with provider {
        RegisterMerchantUseCase(instance(), instance(), instance("createMerchantDataSource"))
    }

}