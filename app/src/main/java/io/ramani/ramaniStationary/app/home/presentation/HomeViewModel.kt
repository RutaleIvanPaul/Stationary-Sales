package io.ramani.ramaniStationary.app.home.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.home.flow.HomeFlow
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.data.home.models.request.DailySalesStatsRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetMerchantRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetProductRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetTaxRequestModel
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider

class HomeViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val dailySalesStatsUseCase: BaseSingleUseCase<List<DailySalesStatsModel>, DailySalesStatsRequestModel>,
    private val getTaxesUseCase: BaseSingleUseCase<List<TaxModel>, GetTaxRequestModel>,
    private val getProductsUseCase: BaseSingleUseCase<List<ProductModel>, GetProductRequestModel>,
    private val getMerchantsUseCase: BaseSingleUseCase<List<MerchantModel>, GetMerchantRequestModel>,
    private val prefs: PrefsManager

) : BaseViewModel(application, stringProvider, sessionManager) {
    val validationResponseLiveData = MutableLiveData<Pair<Boolean, Boolean>>()
    val loginActionLiveData = MutableLiveData<UserModel>()
    private lateinit var flow: HomeFlow
    override fun start(args: Map<String, Any?>) {

    }

    /*
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
                prefs.accountType = it.accountType
                prefs.timeZone = it.timeZone
                loginActionLiveData.postValue(it)
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
    */

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val dailySalesStatsUseCase: BaseSingleUseCase<List<DailySalesStatsModel>, DailySalesStatsRequestModel>,
        private val getTaxesUseCase: BaseSingleUseCase<List<TaxModel>, GetTaxRequestModel>,
        private val getProductsUseCase: BaseSingleUseCase<List<ProductModel>, GetProductRequestModel>,
        private val getMerchantsUseCase: BaseSingleUseCase<List<MerchantModel>, GetMerchantRequestModel>,
        private val prefs: PrefsManager
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    dailySalesStatsUseCase, getTaxesUseCase, getProductsUseCase, getMerchantsUseCase,
                    prefs
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}