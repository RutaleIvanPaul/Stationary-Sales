package io.ramani.ramaniStationary.app.createorder.presentation

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.data.createorder.models.request.GetAvailableStockRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetMerchantRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetProductRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetTaxRequestModel
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.base.SingleLiveEvent
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createorder.model.AvailableProductModel
import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel
import io.ramani.ramaniStationary.domain.datetime.DateFormatter
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.NumberFormat
import java.util.*

class CreateOrderViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val getTaxesUseCase: BaseSingleUseCase<PagedList<TaxModel>, GetTaxRequestModel>,
    private val getProductsUseCase: BaseSingleUseCase<PagedList<ProductModel>, GetProductRequestModel>,
    private val getMerchantsUseCase: BaseSingleUseCase<PagedList<MerchantModel>, GetMerchantRequestModel>,
    private val getAvailableStockUseCase: BaseSingleUseCase<List<AvailableStockModel>, GetAvailableStockRequestModel>,
    private val prefs: PrefsManager,
    private val dateFormatter: DateFormatter
) : BaseViewModel(application, stringProvider, sessionManager) {

    var userId = ""
    var companyId = ""

    val merchantList = mutableListOf<MerchantModel>()
    val onMerchantsLoadedLiveData = SingleLiveEvent<List<MerchantModel>>()

    val productList = mutableListOf<ProductModel>()
    val onProductsLoadedLiveData = SingleLiveEvent<List<ProductModel>>()

    val taxesList = mutableListOf<TaxModel>()
    val onTaxesLoadedLiveData = SingleLiveEvent<List<TaxModel>>()

    val availableStockProductList = mutableListOf<AvailableProductModel>()
    val onAvailableStockProductsLoadedLiveData = SingleLiveEvent<List<AvailableProductModel>>()

    var calendar: Calendar = Calendar.getInstance()
    private var date = Date()

    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            userId = it.uuid
            companyId = it.companyId

            getProducts()
            getAvailableStockList()
        }
    }

    @SuppressLint("CheckResult")
    fun getMerchants() {
        isLoadingVisible = true

        merchantList.clear()

        sessionManager.getLoggedInUser().subscribeBy {
            val single = getMerchantsUseCase.getSingle(GetMerchantRequestModel(false, companyId, "", "", true, 1))
            subscribeSingle(single, onSuccess = {
                merchantList.addAll(it.data)
                onMerchantsLoadedLiveData.postValue(merchantList)

            }, onError = {

            })
        }
    }

    @SuppressLint("CheckResult")
    fun getProducts() {
        productList.clear()

        sessionManager.getLoggedInUser().subscribeBy {
            val single = getProductsUseCase.getSingle(GetProductRequestModel(false, companyId, "", "",false, 1))
            subscribeSingle(single, onSuccess = {
                productList.addAll(it.data)
                onProductsLoadedLiveData.postValue(productList)

            }, onError = {

            })
        }
    }

    @SuppressLint("CheckResult")
    fun getTaxes() {
        isLoadingVisible = true
        taxesList.clear()

        sessionManager.getLoggedInUser().subscribeBy {
            val single = getTaxesUseCase.getSingle(GetTaxRequestModel(false, companyId, userId, "", 1))
            subscribeSingle(single, onSuccess = {
                taxesList.addAll(it.data)
                onTaxesLoadedLiveData.postValue(taxesList)
            }, onError = {

            })
        }
    }

    @SuppressLint("CheckResult")
    fun getAvailableStockList() {
        isLoadingVisible = true
        availableStockProductList.clear()

        sessionManager.getLoggedInUser().subscribeBy {
            val single = getAvailableStockUseCase.getSingle(GetAvailableStockRequestModel(userId))
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false

                it.forEach {
                    availableStockProductList.addAll(it.products)
                }

                onAvailableStockProductsLoadedLiveData.postValue(availableStockProductList)

            }, onError = {

            })
        }
    }

    fun getFormattedAmount(amount: Int): String = NumberFormat.getNumberInstance(Locale.US).format(amount)

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getTaxesUseCase: BaseSingleUseCase<PagedList<TaxModel>, GetTaxRequestModel>,
        private val getProductsUseCase: BaseSingleUseCase<PagedList<ProductModel>, GetProductRequestModel>,
        private val getMerchantsUseCase: BaseSingleUseCase<PagedList<MerchantModel>, GetMerchantRequestModel>,
        private val getAvailableStockUseCase: BaseSingleUseCase<List<AvailableStockModel>, GetAvailableStockRequestModel>,
        private val prefs: PrefsManager,
        private val dateFormatter: DateFormatter
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateOrderViewModel::class.java)) {
                return CreateOrderViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    getTaxesUseCase, getProductsUseCase, getMerchantsUseCase,
                    getAvailableStockUseCase,
                    prefs,
                    dateFormatter
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}