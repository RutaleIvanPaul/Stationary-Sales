package io.ramani.ramaniStationary.domain.base

import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.reactivex.Flowable

/**
 * Created by Amr on 9/4/17.
 */
abstract class BaseFlowableUseCase<T> protected constructor(
    protected val threadExecutor: ThreadExecutor,
    protected val postThreadExecutor: PostThreadExecutor
) : FlowableUseCase<T> {
    protected abstract fun buildUseCaseFlowable(params: Params): Flowable<T>

    override fun getFlowable(params: Params): Flowable<T> =
        buildUseCaseFlowable(params).subscribeOn(threadExecutor.scheduler)
            .observeOn(postThreadExecutor.scheduler)
}