package io.ramani.ramaniStationary.app.createmerchant.presentation

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.common.presentation.errors.PresentationError
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.data.createmerchant.models.request.GetTopPerformersRequestModel
import io.ramani.ramaniStationary.data.createmerchant.models.request.RegisterMerchantRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetMerchantRequestModel
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domain.base.SingleLiveEvent
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createmerchant.model.NameValueModel
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.ramani.ramaniStationary.domain.createmerchant.useCase.RegisterMerchantUseCase
import io.ramani.ramaniStationary.domain.datetime.DateFormatter
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.NumberFormat
import java.util.*

class CreateMerchantViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val getMerchantsUseCase: BaseSingleUseCase<PagedList<MerchantModel>, GetMerchantRequestModel>,
    private val getTopPerformersUseCase: BaseSingleUseCase<TopPerformersModel, GetTopPerformersRequestModel>,
    private val registerMerchantUseCase: BaseSingleUseCase<MerchantModel, RegisterMerchantRequestModel>,
    private val prefs: PrefsManager,
    val dateFormatter: DateFormatter
) : BaseViewModel(application, stringProvider, sessionManager) {

    var userModel: UserModel = UserModel()
    var userId = ""
    var companyId = ""

    val merchantList = mutableListOf<MerchantModel>()
    val merchantNameList = mutableListOf<String>()
    val onMerchantsLoadedLiveData = SingleLiveEvent<List<MerchantModel>>()

    val topSalePeoples = mutableListOf<NameValueModel>()
    val topMerchants = mutableListOf<NameValueModel>()
    val onTopPerformersLoadedLiveData = SingleLiveEvent<TopPerformersModel>()

    val onMerchantAddedLiveData = SingleLiveEvent<Pair<MerchantModel?, String>>()

    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            userModel = it
            userId = it.uuid
            companyId = it.companyId

            getMerchants()
            getTopPerformers()
        }
    }

    @SuppressLint("CheckResult")
    fun getMerchants() {
        merchantList.clear()
        merchantNameList.clear()

        sessionManager.getLoggedInUser().subscribeBy { user ->
            val single = getMerchantsUseCase.getSingle(GetMerchantRequestModel(false, user.companyId, "", "", true, 1))
            subscribeSingle(single, onSuccess = {
                merchantList.addAll(it.data)

                merchantList.forEach { merchant ->
                    merchantNameList.add(merchant.name)
                }

                onMerchantsLoadedLiveData.postValue(merchantList)

            }, onError = {
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
            })
        }
    }

    @SuppressLint("CheckResult")
    fun getTopPerformers() {
        isLoadingVisible = true

        topSalePeoples.clear()
        topMerchants.clear()

        val startDate = "1970-01-01T00:00:00.000Z"
        val endDate = dateFormatter.getTimeWithFormmatter(Date(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        sessionManager.getLoggedInUser().subscribeBy { user ->
            val single = getTopPerformersUseCase.getSingle(GetTopPerformersRequestModel(user.companyId, startDate, endDate, 1000))
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false

                topSalePeoples.addAll(it.topSalesPeople)
                topMerchants.addAll(it.topMerchants)

                onTopPerformersLoadedLiveData.postValue(it)
            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
            })
        }
    }

    @SuppressLint("CheckResult")
    fun registerMerchant(merchant: RegisterMerchantRequestModel) {
        isLoadingVisible = true

        sessionManager.getLoggedInUser().subscribeBy { user ->
            val single = registerMerchantUseCase.getSingle(merchant)
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false

                merchantList.add(it)

                onMerchantAddedLiveData.postValue(Pair(first = it, second = ""))
            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
                onMerchantAddedLiveData.postValue(Pair(first = null, second = getErrorMessage(it)))
            })
        }
    }

    fun getFormattedAmount(amount: Int): String = NumberFormat.getNumberInstance(Locale.US).format(amount)

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getMerchantsUseCase: BaseSingleUseCase<PagedList<MerchantModel>, GetMerchantRequestModel>,
        private val getTopPerformersUseCase: BaseSingleUseCase<TopPerformersModel, GetTopPerformersRequestModel>,
        private val registerMerchantUseCase: BaseSingleUseCase<MerchantModel, RegisterMerchantRequestModel>,
        private val prefs: PrefsManager,
        private val dateFormatter: DateFormatter
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateMerchantViewModel::class.java)) {
                return CreateMerchantViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    getMerchantsUseCase,
                    getTopPerformersUseCase,
                    registerMerchantUseCase,
                    prefs,
                    dateFormatter
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}