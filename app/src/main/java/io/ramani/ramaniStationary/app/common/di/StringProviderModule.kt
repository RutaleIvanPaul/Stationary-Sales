package io.ramani.ramaniStationary.app.common.di

import io.ramani.ramaniStationary.app.common.presentation.language.StringProvider
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val stringProviderModule = Kodein.Module("stringProviderModule") {
    bind<IStringProvider>() with singleton {
        StringProvider
    }
}