package io.ramani.ramaniStationary.app.createorder.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.createorder.presentation.CreateOrderViewModel
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val createOrderModule = Module("createOrderModule") {

    import(createOrderDataModule)
    import(createOrderDomainModule)

    bind<CreateOrderViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, CreateOrderViewModel.Factory(
                instance(), instance(), instance(),
                instance("GetTaxesUseCase"), instance("GetProductsUseCase"), instance("GetMerchantsUseCase"),
                instance("GetAvailableStockUseCase"),
                instance("RegisterMerchantUseCase"),
                instance(),
                instance(),
                instance(),
                instance()
            )
        ).get(CreateOrderViewModel::class.java)
    }

}