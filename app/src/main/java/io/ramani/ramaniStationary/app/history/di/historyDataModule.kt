package io.ramani.ramaniStationary.app.history.di

import io.ramani.ramaniStationary.data.common.network.ServiceHelper
import io.ramani.ramaniStationary.data.history.HistoryApi
import io.ramani.ramaniStationary.data.history.HistoryRemoteDataSource
import io.ramani.ramaniStationary.domain.history.HistoryDataSource
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val historyDataModule = Kodein.Module("historyDataModule") {

    bind<HistoryDataSource>("historyRemoteDataSource") with singleton {
        HistoryRemoteDataSource(instance("historyApi"))
    }

    bind<HistoryApi>("historyApi") with provider {
        ServiceHelper.createService(instance())
    }
}