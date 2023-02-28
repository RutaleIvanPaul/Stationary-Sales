package io.ramani.ramaniStationary.app.common.presentation.viewmodels

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.ramani.ramaniStationary.App
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.download.IFilesDownloadManager
import io.ramani.ramaniStationary.app.common.presentation.errors.PresentationError
import io.ramani.ramaniStationary.app.entities.ValidationError
import io.ramani.ramaniStationary.app.main.presentation.MainActivity
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.base.SingleLiveEvent
import io.ramani.ramaniStationary.domain.entities.exceptions.TokenAlreadyRefreshedException
import io.ramani.ramaniStationary.domainCore.observers.DefaultCompletableObserver
import io.ramani.ramaniStationary.domainCore.observers.DefaultMayeObserver
import io.ramani.ramaniStationary.domainCore.observers.DefaultObserver
import io.ramani.ramaniStationary.domainCore.observers.DefaultSingleObserver
import io.ramani.ramaniStationary.domainCore.presentation.ErrorHandlerView
import io.ramani.ramaniStationary.domainCore.presentation.GenericErrorHandlerView
import io.ramani.ramaniStationary.domainCore.presentation.GenericErrors
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.json.JSONArray
import org.json.JSONObject
import org.kodein.di.generic.instance
import retrofit2.HttpException
import kotlin.properties.Delegates

/**
 * Created by Amr on 9/8/17.
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    constructor(
        application: Application,
        stringProvider: IStringProvider,
        sessionManager: ISessionManager
    ) : this(application) {
        this.stringProvider = stringProvider
        this.sessionManager = sessionManager
    }

    companion object {
        const val SUCCESS_MSG_DELAY_MILLIS = 1500L
    }

    val isLoadingVisibleObservable: PublishSubject<Boolean> = PublishSubject.create()
    val loadingLiveData = MutableLiveData<Boolean>()
    val isFullScreenLoadingVisibleObservable: PublishSubject<Boolean> = PublishSubject.create()
    val isEmptyObservable: PublishSubject<Boolean> = PublishSubject.create()
    val isEmptyLiveData = MutableLiveData<Boolean>()
    val loadingErrorObservable: PublishSubject<String> = PublishSubject.create()
    val errorObservable: PublishSubject<PresentationError> = PublishSubject.create()
    val errorLiveData = MutableLiveData<PresentationError>()
    val validationErrorsObservable: PublishSubject<List<ValidationError>> = PublishSubject.create()
    val validationErrorsLiveData = MutableLiveData<List<ValidationError>>()
    val confirmLogoutObservable: PublishSubject<String> = PublishSubject.create()
    val confirmRestartObservable: PublishSubject<String> = PublishSubject.create()
    val navigationEventLiveData = SingleLiveEvent<NavigationEvent>()

    val toolbarTitleLiveData: MutableLiveData<String> = MutableLiveData()
    val toolbarEnableShareLiveData: MutableLiveData<Pair<Boolean, () -> Unit>> = MutableLiveData()
    val toolbarEnableBackButtonLiveData: MutableLiveData<Pair<Boolean, Boolean>> = MutableLiveData()

    protected open lateinit var sessionManager: ISessionManager
    protected open lateinit var stringProvider: IStringProvider

    private val errorHandler: ErrorHandlerView? by lazy {
        GenericErrorHandlerView(onError = { code, message ->
            onGenericError(code, message)
        })
    }

    private val defaultOnLogout: (String?) -> Unit = { message ->
        if (message?.isNotBlank() == true) {
            confirmLogoutObservable.onNext(message)
        } else {
            val app = getApplication<Application>()
            val intent = Intent(app, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            app.startActivity(intent)
        }
    }

    private val defaultOnPermissionsChanged: (String?) -> Unit = { message ->
        if (message?.isNotBlank() == true) {
            confirmRestartObservable.onNext(message)
        } else {
            restart()
        }
    }

    protected open var isLoadingVisible: Boolean by Delegates.observable(false) { _, _, newValue ->
        isLoadingVisibleObservable.onNext(newValue)
        loadingLiveData.postValue(newValue)
    }

    protected open var isFullScreenLoadingVisible: Boolean by Delegates.observable(false) { _, _, newValue ->
        isFullScreenLoadingVisibleObservable.onNext(newValue)
    }

    protected open var isEmpty: Boolean by Delegates.observable(false) { _, _, newValue ->
        isEmptyObservable.onNext(newValue)

    }

    protected fun getString(@StringRes stringResId: Int) =
        stringProvider.getString(stringResId)

    @RequiresApi(Build.VERSION_CODES.M)
    protected fun getColor(@ColorRes colorResId: Int) =
        getApplication<App>().getColor(colorResId)

    protected fun isDualPane() =
        getApplication<Application>().resources.getBoolean(R.bool.isTablet)

    protected fun getErrorMessage(throwable: Throwable): String {
//        return onError(throwable as HttpException)
        return throwable.message?.takeIf { it.isNotBlank() }
            ?: getString(R.string.an_error_has_occured_please_try_again)
    }

    protected fun onError(throwable: Throwable, handleError: (Throwable) -> Unit = {}) {
        if (errorHandler?.handleError(throwable) == false) {
            handleError(throwable)
        }
    }

    protected fun onError(throwable: HttpException): String {
        var message = ""
        val response = throwable.response().errorBody()?.string()
        val jsonObj = JSONObject(response)
        var errors = jsonObj["errors"] as JSONObject
        for (error in errors.keys()) {
            var arrayOfErrors = errors[error] as JSONArray
            for (index in 0 until arrayOfErrors.length()) {
                message += (arrayOfErrors.get(index))
                message += ("\n")
            }
        }
        return message
    }

    protected fun notifyError(error: String, type: Int) {
        errorObservable.onNext(PresentationError(error, type))
    }

    protected fun notifyErrorObserver(error: String, type: Int) {
        errorLiveData.postValue(PresentationError(error, type))
    }

    protected fun notifyValidationErrors(errors: List<ValidationError>) {
        validationErrorsObservable.onNext(errors)
    }

    protected fun notifyValidationErrorsObservers(errors: List<ValidationError>) {
        validationErrorsLiveData.postValue(errors)
    }

    protected fun notifyNavigationEvent(event: NavigationEvent) {
        navigationEventLiveData.postValue(event)
    }


    fun isUserLoggedIn() = sessionManager.isUserLoggedIn()

    fun getLoggedInUser() = sessionManager.getLoggedInUser()

    fun logout(message: String? = "", onLogOutComplete: (String?) -> Unit = defaultOnLogout) {
        subscribeSingleFromMainThread(sessionManager.logout(), onSuccess = {
            onLogOutComplete(message)
        })
    }

    fun refreshToken(token: String) = sessionManager.refreshAccessToken(token)


    protected fun <T> subscribeObservable(
        observable: Observable<T>,
        onNext: (T) -> Unit = {},
        onComplete: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ): Disposable =
        observable.retryWhen { errors ->
            errors.flatMap {
                when (it) {
//                    is TokenExpiredException -> onTokenExpiredError()
//                        .andThen(Observable.fromCallable { true })
                    is TokenAlreadyRefreshedException ->
                        Observable.fromCallable { true }
                    else -> Observable.error(it)
                }
            }
        }.subscribeWithErrorHandler(onNext, onComplete, onError, errorHandler)

    protected fun <T> subscribeSingle(
        single: Single<T>,
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ): Disposable =
        single.retryWhen { errors ->
            errors.flatMap {
                when (it) {
//                    is TokenExpiredException ->
//                        onTokenExpiredError().andThen(Flowable.fromCallable { true })
                    is TokenAlreadyRefreshedException ->
                        Flowable.fromCallable { true }
                    else -> Flowable.error(it)
                }
            }
        }.subscribeWithErrorHandler(onSuccess, onError, errorHandler)

    protected fun <T> subscribeSingleFromMainThread(
        single: Single<T>,
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ): Disposable =
        single.retryWhen { errors ->
            errors.flatMap {
                when (it) {
//                    is TokenExpiredException ->
//                        onTokenExpiredError().andThen(Flowable.fromCallable { true })
                    is TokenAlreadyRefreshedException ->
                        Flowable.fromCallable { true }
                    else -> Flowable.error(it)
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWithErrorHandler(onSuccess, onError, errorHandler)


    protected fun <T> subscribeMaybe(
        maybe: Maybe<T>,
        onSuccess: (T) -> Unit = {},
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit
    ): Disposable =
        maybe.retryWhen { errors ->
            errors.flatMap {
                when (it) {
//                    is TokenExpiredException ->
//                        onTokenExpiredError().andThen(Flowable.fromCallable { true })
                    is TokenAlreadyRefreshedException ->
                        Flowable.fromCallable { true }
                    else -> Flowable.error(it)
                }
            }
        }.subscribeWithErrorHandler(onSuccess, onComplete, onError, errorHandler)

    protected fun subscribeCompletable(
        completable: Completable,
        onComplete: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ): Disposable =
        completable.retryWhen { errors ->
            errors.flatMap {
                when (it) {
//                    is TokenExpiredException ->
//                        onTokenExpiredError().andThen(Flowable.fromCallable { true })
                    is TokenAlreadyRefreshedException ->
                        Flowable.fromCallable { true }
                    else -> Flowable.error(it)
                }
            }
        }.subscribeWithErrorHandler(onComplete, onError, errorHandler)

    private fun <T> Observable<T>.subscribeWithErrorHandler(
        onNext: (T) -> Unit = {},
        onComplete: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
        errorHandler: ErrorHandlerView?
    ): Disposable =
        subscribeWith(DefaultObserver(onNext, onComplete, onError, errorHandler))

    private fun <T> Single<T>.subscribeWithErrorHandler(
        onSuccess: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        errorHandler: ErrorHandlerView?
    ): Disposable =
        subscribeWith(DefaultSingleObserver(onSuccess, onError, errorHandler))

    private fun <T> Maybe<T>.subscribeWithErrorHandler(
        onSuccess: (T) -> Unit = {},
        onComplete: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
        errorHandler: ErrorHandlerView?
    ): Disposable =
        subscribeWith(DefaultMayeObserver(onSuccess, onComplete, onError, errorHandler))

    private fun Completable.subscribeWithErrorHandler(
        onComplete: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
        errorHandler: ErrorHandlerView?
    ): Disposable =
        subscribeWith(DefaultCompletableObserver(onComplete, onError, errorHandler))

    abstract fun start(args: Map<String, Any?> = mapOf())

    open fun stop() {}

    private fun onGenericError(error: Int, message: String?) {
        when (error) {
            GenericErrors.ERR_SESSION_EXPIRED -> logout(message)
            GenericErrors.ERR_PERMISSIONS_CHANGED -> refreshTokenAndRestart(message)
        }
    }


    private fun onTokenExpiredError() {}

    private fun refreshTokenAndRestart(message: String? = "") {
//        subscribeCompletable(onTokenExpiredError(), onComplete = {
//            defaultOnPermissionsChanged(message)
//        })
    }


    fun downloadFie(url: String, fileName: String, mimeType: String, onComplete: (Uri) -> Unit) {
        val downloadManager: IFilesDownloadManager by getApplication<App>().kodein.instance()
        downloadManager.enqueue(url, fileName, mimeType) {
            onComplete(it)
        }
    }

    fun restart() {
        val app = getApplication<Application>()
        val intent = Intent(app, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        app.startActivity(intent)
    }
}