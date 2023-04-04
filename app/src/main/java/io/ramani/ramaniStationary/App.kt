package io.ramani.ramaniStationary

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import io.ramani.ramaniStationary.app.common.di.appModule
import io.ramani.ramaniStationary.app.common.download.DownloadManager
import io.ramani.ramaniStationary.app.common.download.FilesDownloadManager
import io.ramani.ramaniStationary.app.common.download.IFilesDownloadManager
import io.ramani.ramaniStationary.app.common.download.IMediaDownloadManager
import io.ramani.ramaniStationary.app.common.presentation.actvities.BaseActivityLifeCycleCallbacks
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.domainCore.prefs.Prefs
import io.ramani.ramaniStationary.domainCore.printer.PX400Printer
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

/**
 * Created by Amr on 9/7/17.
 */
class App : Application(), KodeinAware {
    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@App))
        bind<Prefs>() with singleton { PrefsManager(this@App) }
        bind<IMediaDownloadManager>() with singleton {
            DownloadManager(
                this@App,
                instance(),
                instance()
            )
        }
        bind<IFilesDownloadManager>() with singleton { FilesDownloadManager(this@App, instance()) }
        bind<PX400Printer>() with singleton { PX400Printer(this@App) }
        import(appModule)
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        initPusNotificationsManager()
        registerActivityLifecycleCallbacks(BaseActivityLifeCycleCallbacks())
    }


    private fun initPusNotificationsManager() {
        //TODO: Setup firebase here
    }

//    private fun initiateMobiIoTAPI(){
//        if(Build.MANUFACTURER == Manufacturer.MobiIot.name ||Build.MANUFACTURER == Manufacturer.MobiWire.name){
//            MobiiotAPI(this)
//
//        }
//    }


}