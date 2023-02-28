package io.ramani.ramaniStationary.domainCore.observers

import io.ramani.ramaniStationary.core.domain.observers.BaseSingleObserver
import io.ramani.ramaniStationary.domainCore.presentation.ErrorHandlerView


/**
 * Created by Amr on 10/27/17.
 */
class DefaultSingleObserver<T>(
    doOnSuccess: (T) -> Unit = {},
    doOnError: (Throwable) -> Unit = {},
    private val errorHandler: ErrorHandlerView?
) : BaseSingleObserver<T>(doOnSuccess, doOnError) {
    override fun onError(e: Throwable) {
        if (errorHandler?.handleError(e) == false) {
            super.onError(e)
        }
    }
}