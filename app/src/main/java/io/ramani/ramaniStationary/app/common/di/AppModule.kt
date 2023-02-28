package io.ramani.ramaniStationary.app.common.di


import io.ramani.ramaniStationary.app.auth.di.authModule
import io.ramani.ramaniStationary.app.common.di.datetime.dateTimeModule
import io.ramani.ramaniStationary.app.common.di.pagination.paginationModule
import io.ramani.ramaniStationary.app.main.di.mainModule

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
    import(mainModule)
    import(authModule)

}
