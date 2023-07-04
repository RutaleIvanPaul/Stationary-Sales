package io.ramani.ramaniStationary.core.domain.observers

import io.ramani.ramaniStationary.app.main.presentation.MAIN_SHARED_MODEL
import io.reactivex.observers.DisposableSingleObserver

/**
 * Created by Amr on 10/27/17.
 */
open class BaseSingleObserver<T>(
    private val doOnSuccess: (T) -> Unit = {},
    private val doOnError: (Throwable) -> Unit = {}
) : DisposableSingleObserver<T>() {
    override fun onSuccess(t: T) {
        //[2023.4.13][Adrian] If network call is success, then it should be notified to show off "Offline" badge
        MAIN_SHARED_MODEL.updateNetworkStatus(true)

        doOnSuccess(t)
    }

    override fun onError(e: Throwable) {
        doOnError(e)
    }
}