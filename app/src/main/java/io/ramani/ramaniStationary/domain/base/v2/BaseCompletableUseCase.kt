package io.ramani.ramaniStationary.domain.base.v2

import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.reactivex.Completable

abstract class BaseCompletableUseCase<P : Params> protected constructor(
    protected val threadExecutor: ThreadExecutor,
    protected val postThreadExecutor: PostThreadExecutor
) : CompletableUseCase<P> {
    protected abstract fun buildUseCaseCompletable(params: P?): Completable

    override fun getCompletable(params: P?): Completable =
        buildUseCaseCompletable(params)
            .subscribeOn(threadExecutor.scheduler)
            .observeOn(postThreadExecutor.scheduler)
}