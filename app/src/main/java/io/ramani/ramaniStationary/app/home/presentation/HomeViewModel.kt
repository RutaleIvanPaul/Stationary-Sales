package io.ramani.ramaniStationary.app.home.presentation

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.common.presentation.errors.PresentationError
import io.ramani.ramaniStationary.app.home.flow.HomeFlow
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.data.home.models.request.DailySalesStatsRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetMerchantRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetProductRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetTaxRequestModel
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.base.SingleLiveEvent
import io.ramani.ramaniStationary.domain.base.exceptions.ItemNotFoundException
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.model.DailySalesStatsModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy

class HomeViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val dailySalesStatsUseCase: BaseSingleUseCase<PagedList<DailySalesStatsModel>, DailySalesStatsRequestModel>,
    private val getTaxesUseCase: BaseSingleUseCase<PagedList<TaxModel>, GetTaxRequestModel>,
    private val getProductsUseCase: BaseSingleUseCase<PagedList<ProductModel>, GetProductRequestModel>,
    private val getMerchantsUseCase: BaseSingleUseCase<PagedList<MerchantModel>, GetMerchantRequestModel>,
    private val prefs: PrefsManager
) : BaseViewModel(application, stringProvider, sessionManager) {

    var userId = ""
    var companyId = ""

    val dailySalesStatsActionLiveData = MutableLiveData<List<DailySalesStatsModel>>()

    val taxesList = mutableListOf<TaxModel>()
    val onTaxesLoadedLiveData = SingleLiveEvent<Boolean>()
    private var hasMoreTaxesToLoad = true
    private var taxPage = 1

    val merchantList = mutableListOf<MerchantModel>()
    val onMerchantsLoadedLiveData = SingleLiveEvent<Boolean>()
    private var hasMoreMerchantsToLoad = true
    private var merchantPage = 1

    val productList = mutableListOf<ProductModel>()
    val onProductsLoadedLiveData = SingleLiveEvent<Boolean>()
    private var hasMoreProductsToLoad = true
    private var productPage = 1

    private lateinit var flow: HomeFlow
    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {
        isLoadingVisible = true
        sessionManager.getLoggedInUser().subscribeBy {
            userId = it.uuid
            companyId = it.companyId
        }
    }

    fun getDailySalesStats(startDate: String, endDate: String) {
        isLoadingVisible = true

        val single = dailySalesStatsUseCase.getSingle(DailySalesStatsRequestModel(companyId, 1, startDate, endDate))
        subscribeSingle(single, onSuccess = {
            isLoadingVisible = false

            dailySalesStatsActionLiveData.postValue(it.data)
        }, onError = {
            isLoadingVisible = false
//                notifyError(
//                    it.message
//                        ?: getString(R.string.an_error_has_occured_please_try_again),
//                    PresentationError.ERROR_TEXT
//                )
            notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
        })
    }

    fun syncData(datetime: String) {
        // Get last sync data
        val lastSyncTime = prefs.lastSyncTime
        getTaxes(lastSyncTime)
        getProducts(lastSyncTime)
        getMerchants(lastSyncTime)

        prefs.lastSyncTime = datetime
    }

    @SuppressLint("CheckResult")
    fun getTaxes(date: String) {
        if (hasMoreTaxesToLoad) {
            isLoadingVisible = true
            if (taxPage == 1) {
                taxesList.clear()
            }

            sessionManager.getLoggedInUser().subscribeBy {
                val single = getTaxesUseCase.getSingle(GetTaxRequestModel(companyId, userId, date, taxPage))
                subscribeSingle(single, onSuccess = {
                    isLoadingVisible = false
                    if (it.data.isEmpty()) {
                        onTaxesLoadedLiveData.postValue(false)
                    } else {
                        taxesList.addAll(it.data)
                        hasMoreTaxesToLoad = it.paginationMeta.hasNext
                        onTaxesLoadedLiveData.postValue(true)
                    }
                }, onError = {
                    isLoadingVisible = false
                    if (it is ItemNotFoundException) {
                        hasMoreTaxesToLoad = false
                    } else {
                        notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
                    }
                })
            }
        }
    }

    @SuppressLint("CheckResult")
    fun getMerchants(date: String) {
        if (hasMoreMerchantsToLoad) {
            isLoadingVisible = true
            if (merchantPage == 1) {
                merchantList.clear()
            }

            sessionManager.getLoggedInUser().subscribeBy {
                val single = getMerchantsUseCase.getSingle(GetMerchantRequestModel(date, true, merchantPage))
                subscribeSingle(single, onSuccess = {
                    isLoadingVisible = false
                    if (it.data.isEmpty()) {
                        onMerchantsLoadedLiveData.postValue(false)
                    } else {
                        merchantList.addAll(it.data)
                        hasMoreMerchantsToLoad = it.paginationMeta.hasNext
                        onMerchantsLoadedLiveData.postValue(true)
                    }
                }, onError = {
                    isLoadingVisible = false
                    if (it is ItemNotFoundException) {
                        hasMoreMerchantsToLoad = false
                    } else {
                        notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
                    }
                })
            }
        }
    }

    @SuppressLint("CheckResult")
    fun getProducts(date: String) {
        if (hasMoreProductsToLoad) {
            isLoadingVisible = true
            if (productPage == 1) {
                productList.clear()
            }

            sessionManager.getLoggedInUser().subscribeBy {
                val single = getProductsUseCase.getSingle(GetProductRequestModel(companyId, date, false, productPage))
                subscribeSingle(single, onSuccess = {
                    isLoadingVisible = false
                    if (it.data.isEmpty()) {
                        onProductsLoadedLiveData.postValue(false)
                    } else {
                        productList.addAll(it.data)
                        hasMoreProductsToLoad = it.paginationMeta.hasNext
                        onProductsLoadedLiveData.postValue(true)
                    }
                }, onError = {
                    isLoadingVisible = false
                    if (it is ItemNotFoundException) {
                        hasMoreProductsToLoad = false
                    } else {
                        notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
                    }
                })
            }
        }
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val dailySalesStatsUseCase: BaseSingleUseCase<PagedList<DailySalesStatsModel>, DailySalesStatsRequestModel>,
        private val getTaxesUseCase: BaseSingleUseCase<PagedList<TaxModel>, GetTaxRequestModel>,
        private val getProductsUseCase: BaseSingleUseCase<PagedList<ProductModel>, GetProductRequestModel>,
        private val getMerchantsUseCase: BaseSingleUseCase<PagedList<MerchantModel>, GetMerchantRequestModel>,
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