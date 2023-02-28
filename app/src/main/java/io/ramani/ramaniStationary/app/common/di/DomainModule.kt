package io.ramani.ramaniStationary.app.common.di

import io.ramani.ramaniStationary.domain.base.executor.BackgroundThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.ramani.ramaniStationary.domain.executor.UiThreadExecutor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

/**
 * Created by Amr on 9/11/17.
 */
val domainModule = Kodein.Module("domainModule", false,"", fun Kodein.Builder.() {
    bind<ThreadExecutor>() with provider { BackgroundThreadExecutor() }
    bind<PostThreadExecutor>() with provider { UiThreadExecutor() }
})