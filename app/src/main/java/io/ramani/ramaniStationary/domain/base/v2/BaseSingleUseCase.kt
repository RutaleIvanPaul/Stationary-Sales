package io.ramani.ramaniStationary.domain.base.v2

import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.reactivex.Single

/**
 * Created by Amr on 9/4/17.
 */
abstract class BaseSingleUseCase<T, P : Params> protected constructor(
    protected val threadExecutor: ThreadExecutor,
    protected val postThreadExecutor: PostThreadExecutor
) : SingleUseCase<T, P> {
    protected abstract fun buildUseCaseSingle(params: P?): Single<T>

    override fun getSingle(params: P?): Single<T> =
        buildUseCaseSingle(params).subscribeOn(threadExecutor.scheduler)
            .observeOn(postThreadExecutor.scheduler)
}