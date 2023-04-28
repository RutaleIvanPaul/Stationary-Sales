package io.ramani.ramaniStationary.app.credit.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.credit.presentation.CreditViewModel
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val creditModule = Module("creditModule") {

    import(creditDataModule)
    import(creditDomainModule)

    bind<CreditViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, CreditViewModel.Factory(
                instance(), instance(), instance(),
                instance("GetListLocationsUseCase"),
                instance("GetOrderDetailsUseCase"),
                instance("UpdateOrderPaymentStatusUseCase"),
                instance(),
                instance(),
                instance()
            )
        ).get(CreditViewModel::class.java)
    }

}