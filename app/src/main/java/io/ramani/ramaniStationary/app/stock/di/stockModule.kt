package io.ramani.ramaniStationary.app.stock.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.stock.presentation.StockViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val stockModule = Kodein.Module("stockModule"){
    import(stockDataModule)
    import(stockDomainModule)

    bind<StockViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, StockViewModel.Factory(
                instance(), instance(), instance(),
                instance("AvailableStockUseCase")
            )
        ).get(StockViewModel::class.java)

    }
}