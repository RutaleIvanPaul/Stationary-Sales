package io.ramani.ramaniStationary.app.auth.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.auth.flow.AuthFlow
import io.ramani.ramaniStationary.app.common.presentation.errors.PresentationError
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.auth.models.LoginRequestModel
import io.ramani.ramaniStationary.data.auth.models.TaxInformationResponse
import io.ramani.ramaniStationary.data.auth.models.request.TaxInformationRequest
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider

class LoginViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val loginUseCase: BaseSingleUseCase<UserModel, LoginRequestModel>,
    private val taxObjectUseCase: BaseSingleUseCase<TaxInformationResponse, TaxInformationRequest>,
    private val prefs: PrefsManager

) : BaseViewModel(application, stringProvider, sessionManager) {
    val validationResponseLiveData = MutableLiveData<Pair<Boolean, Boolean>>()
    val loginActionLiveData = MutableLiveData<UserModel>()
    private lateinit var flow: AuthFlow
    override fun start(args: Map<String, Any?>) {

    }

    fun login(selectedCountryCode: Int, phone: String?, password: String?) {
        if (phone.isNullOrBlank() || password.isNullOrBlank()) {
            validationResponseLiveData.postValue(
                Pair(
                    !phone.isNullOrBlank(),
                    !password.isNullOrBlank()
                )
            )
        } else {
            validationResponseLiveData.postValue(Pair(first = true, second = true))
            isLoadingVisible = true
            var phoneEnhanced = phone
            if (phoneEnhanced.toCharArray()[0] == '0')
                phoneEnhanced = phoneEnhanced.replaceFirst("0", "")// remove first character
            phoneEnhanced = "$selectedCountryCode${phoneEnhanced}"
            val single = loginUseCase.getSingle(LoginRequestModel(phoneEnhanced, password))
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false
                prefs.currentUser = it.toString()
                prefs.accessToken = it.token
                prefs.timeZone = it.timeZone
                loginActionLiveData.postValue(it)

                getTaxObject(it.uuid)
            }, onError = {
                isLoadingVisible = false
//                notifyError(
//                    it.message
//                        ?: getString(R.string.an_error_has_occured_please_try_again),
//                    PresentationError.ERROR_TEXT
//                )
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
                Log.d("ALLAH", "login ERROR: " + it.message)
            })
        }
    }

    fun getTaxObject(uuid: String) {
        val single = taxObjectUseCase.getSingle(TaxInformationRequest(userId = uuid))
        subscribeSingle(single, onSuccess = {
            prefs.taxObject = it.toString()
        },onError = {
            notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
        })
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val loginUseCase: BaseSingleUseCase<UserModel, LoginRequestModel>,
        private val taxObjectUseCase: BaseSingleUseCase<TaxInformationResponse, TaxInformationRequest>,
        private val prefs: PrefsManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    loginUseCase,
                    taxObjectUseCase,
                    prefs
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}