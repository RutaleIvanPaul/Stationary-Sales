package io.ramani.ramaniStationary.domainCore.presentation

/**
 * Created by Amr on 10/27/17.
 */
interface ErrorHandlerView {
    fun handleError(throwable: Throwable): Boolean
}