package io.ramani.ramaniStationary.app.main.presentation

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by Amr on 9/18/17.
 */
class MainViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager
): BaseViewModel(application, stringProvider, sessionManager) {
    var isUserLoggedInBefore = false
    override fun start(args: Map<String, Any?>) {
        getLoggedInUser().subscribeBy {
            isUserLoggedInBefore = !it.token.isNullOrBlank()
        }
    }

    fun isOnline(connectivityManager: ConnectivityManager): Boolean {
        var result = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
}