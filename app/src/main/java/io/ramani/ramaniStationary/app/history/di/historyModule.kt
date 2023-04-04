package io.ramani.ramaniStationary.app.history.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.history.presentation.HistoryViewModel
import io.ramani.ramaniStationary.app.stock.presentation.StockViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val historyModule = Kodein.Module("historyModule"){
    import(historyDomainModule)
    import(historyDataModule)

    bind<HistoryViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, HistoryViewModel.Factory(
                instance(), instance(), instance(),
                instance("GetHistoryUseCase"),
                instance("GetZreportByRangeUseCase"),
                instance("GetXReportUseCase"),
                instance("GetOrderDetailsUseCase"),
                instance(),
                instance()
            )
        ).get(HistoryViewModel::class.java)

    }
}