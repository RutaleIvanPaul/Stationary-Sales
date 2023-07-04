package io.ramani.ramaniStationary.app.common.di


import io.ramani.ramaniStationary.app.auth.di.authModule
import io.ramani.ramaniStationary.app.common.di.datetime.dateTimeModule
import io.ramani.ramaniStationary.app.common.di.pagination.paginationModule
import io.ramani.ramaniStationary.app.createmerchant.di.createMerchantModule
import io.ramani.ramaniStationary.app.createorder.di.createOrderModule
import io.ramani.ramaniStationary.app.credit.di.creditModule
import io.ramani.ramaniStationary.app.history.di.historyModule
import io.ramani.ramaniStationary.app.home.di.homeModule
import io.ramani.ramaniStationary.app.main.di.mainModule
import io.ramani.ramaniStationary.app.reports.di.reportsModule
import io.ramani.ramaniStationary.app.stock.di.stockModule

import org.kodein.di.Kodein

/**
 * Created by Amr on 9/22/17.
 */
val appModule = Kodein.Module("appModule") {
    import(domainModule)
    import(networkModule)
    import(paginationModule)
    import(fileHelperModule)
    import(dateTimeModule)
    import(stringProviderModule)
    import(databaseModule)
    import(mainModule)
    import(authModule)
    import(homeModule)
    import(stockModule)
    import(historyModule)

    import(createOrderModule)
    import(createMerchantModule)
    import(reportsModule)
    import(creditModule)

    import(printerHelperModule)

}
