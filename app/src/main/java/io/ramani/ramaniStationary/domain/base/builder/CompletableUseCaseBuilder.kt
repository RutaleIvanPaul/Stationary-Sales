package io.ramani.ramaniStationary.domain.base.builder

import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.reactivex.Completable

/**
 * Created by Amr on 12/30/17.
 */
abstract class CompletableUseCaseBuilder protected constructor(
    private val threadExecutor: ThreadExecutor, private val postThreadExecutor: PostThreadExecutor
) {
    protected fun buildCompletable(completable: Completable) =
        completable.subscribeOn(threadExecutor.scheduler)
            .observeOn(postThreadExecutor.scheduler)
}