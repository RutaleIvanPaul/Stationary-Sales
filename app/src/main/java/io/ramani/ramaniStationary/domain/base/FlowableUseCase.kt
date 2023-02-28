package io.ramani.ramaniStationary.domain.base

import io.reactivex.Flowable

/**
 * Created by Amr on 9/11/17.
 */
interface FlowableUseCase<T> {
    fun getFlowable(params: Params = emptyParams()): Flowable<T>
}