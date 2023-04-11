package io.ramani.ramaniStationary.app.createmerchant.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.createmerchant.createMerchantDomainModule
import io.ramani.ramaniStationary.app.createmerchant.presentation.CreateMerchantViewModel
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val createMerchantModule = Module("createMerchantModule") {

    import(createMerchantDataModule)
    import(createMerchantDomainModule)

    bind<CreateMerchantViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, CreateMerchantViewModel.Factory(
                instance(), instance(), instance(),
                instance("GetMerchantsUseCase"),
                instance("GetTopPerformersUseCase"),
                instance("RegisterMerchantUseCase"),
                instance(),
                instance()
            )
        ).get(CreateMerchantViewModel::class.java)
    }

}