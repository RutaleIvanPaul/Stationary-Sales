package io.ramani.ramaniStationary.app.common.di


import io.ramani.ramaniStationary.domainCore.io.FileHelper
import io.ramani.ramaniStationary.domainCore.io.IFileHelper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

/**
 * Created by Amr on 11/21/17.
 */
val fileHelperModule = Kodein.Module("fileHelperModule", false,"", fun Kodein.Builder.() {
    bind<IFileHelper>() with singleton {
        FileHelper(instance())
    }
})