package io.ramani.ramaniStationary.app.common.presentation.actvities

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.ramani.ramaniStationary.app.common.debug.DebugHelper
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragmentLifeCycleCallbacks
import io.ramani.ramaniStationary.app.common.presentation.interfaces.DisposablesHolder
import io.ramani.ramaniStationary.app.common.presentation.interfaces.clearDisposables
import io.ramani.ramaniStationary.app.common.presentation.interfaces.disposeAll
import io.ramani.ramaniStationary.domainCore.log.logInfo

/**
 * a class that is responsible for handling common operations during Activity LifeCycle
 *
 * Created by Ahmed Adel Ismail on 2/24/2018.
 */
class BaseActivityLifeCycleCallbacks : Application.ActivityLifecycleCallbacks {


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        logInfo("${activity::class.java.simpleName} created")

        DebugHelper.setCurrentScreen(activity::class.java.simpleName)

        (activity.takeIf { it is AppCompatActivity }
            .let { it as? AppCompatActivity }
            .let { it?.supportFragmentManager } to BaseFragmentLifeCycleCallbacks())
            .also { it.first?.registerFragmentLifecycleCallbacks(it.second, true) }

    }

    override fun onActivityStarted(activity: Activity) {
        // do nothing
    }

    override fun onActivityResumed(activity: Activity) {
        // do nothing
    }

    override fun onActivityPaused(activity: Activity) {
        activity.takeIf { it is DisposablesHolder }
            .let { it as? DisposablesHolder }
            .also { it?.clearDisposables() }
    }

    override fun onActivityStopped(activity: Activity) {
        // do nothing
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // do nothing
    }

    override fun onActivityDestroyed(activity: Activity) {
        logInfo("${activity::class.java.simpleName} destroyed")

        activity.takeIf { it is DisposablesHolder }
            .let { it as? DisposablesHolder }
            .also { it?.disposeAll() }
    }


}