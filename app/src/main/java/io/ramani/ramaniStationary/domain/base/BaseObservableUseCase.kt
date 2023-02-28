package io.ramani.ramaniStationary.domain.base

import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.ramani.ramaniStationary.domain.base.executor.ThreadExecutor
import io.reactivex.Observable

/**
 * Created by Amr on 6/1/17.
 */
abstract class BaseObservableUseCase<T> protected constructor(
    protected val threadExecutor: ThreadExecutor,
    protected val postThreadExecutor: PostThreadExecutor
) : ObservableUseCase<T> {
    protected abstract fun buildUseCaseObservable(params: Params): Observable<T>

    override fun getObservable(params: Params): Observable<T> =
        buildUseCaseObservable(params).subscribeOn(threadExecutor.scheduler)
            .observeOn(postThreadExecutor.scheduler)
}