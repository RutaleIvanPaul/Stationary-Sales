package io.ramani.ramaniStationary.domain.base.builder

import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.reactivex.Single

/**
 * Created by Amr on 12/30/17.
 */
abstract class SingleUseCaseBuilder<T> protected constructor(
    private val threadExecutor: ThreadExecutor, private val postThreadExecutor: PostThreadExecutor
) {
    protected fun buildSingle(single: Single<T>) =
        single.subscribeOn(threadExecutor.scheduler)
            .observeOn(postThreadExecutor.scheduler)
}