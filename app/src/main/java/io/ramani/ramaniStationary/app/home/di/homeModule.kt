package io.ramani.ramaniStationary.app.home.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.home.presentation.HomeViewModel
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val homeModule = Module("homeModule") {

    import(homeDataModule)
    import(homeDomainModule)

    bind<HomeViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, HomeViewModel.Factory(
                instance(), instance(), instance(),
                instance("GetUserAccountDetailsUseCase"),
                instance("GetTaxInformationUseCase"),
                instance("DailySalesStatsUseCase"), instance("GetTaxesUseCase"), instance("GetProductsUseCase"), instance("GetMerchantsUseCase"),
                instance(),
                instance()
            )
        ).get(HomeViewModel::class.java)
    }

}