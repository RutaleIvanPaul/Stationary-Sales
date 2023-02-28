package io.ramani.ramaniStationary.domain.executor

import io.ramani.ramaniStationary.domain.base.executor.PostThreadExecutor
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by Amr on 6/4/17.
 */
class UiThreadExecutor : PostThreadExecutor {
    override val scheduler: Scheduler
        get() = AndroidSchedulers.mainThread()
}