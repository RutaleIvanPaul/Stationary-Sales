package io.ramani.ramaniStationary.app.reports.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.reports.presentation.ReportsViewModel
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val reportsModule = Module("reportsModule") {

    import(reportsDataModule)
    import(reportsDomainModule)

    bind<ReportsViewModel>() with factory { fragment: Fragment ->
        ViewModelProvider(
            fragment, ReportsViewModel.Factory(
                instance(), instance(), instance(),
                instance("GetTopPerformersUseCase"),
                instance(),
                instance(),
                instance()
            )
        ).get(ReportsViewModel::class.java)
    }

}