package io.ramani.ramaniStationary.app.home.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import io.ramani.ramaniStationary.app.common.presentation.errors.PresentationError
import io.ramani.ramaniStationary.app.home.flow.HomeFlow
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.main.presentation.MAIN_SHARED_MODEL
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.data.createorder.models.request.SaleRequestModel
import io.ramani.ramaniStationary.data.database.RamaniDatabase
import io.ramani.ramaniStationary.data.home.models.request.*
import io.ramani.ramaniStationary.data.home.models.response.UserAccountDetailsRemoteModel
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.base.SingleLiveEvent
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createorder.model.SaleModel
import io.ramani.ramaniStationary.domain.datetime.DateFormatter
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.model.*
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.generic.instance
import java.text.NumberFormat
import java.util.*

class HomeViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val getUserAccountDetailsUseCase: BaseSingleUseCase<List<UserAccountDetailsModel>, GetUserAccountDetailsRequestModel>,
    private val getTaxInformationUseCase: BaseSingleUseCase<TaxInformationModel, GetTaxInformationRequestModel>,
    private val dailySalesStatsUseCase: BaseSingleUseCase<PagedList<DailySalesStatsModel>, DailySalesStatsRequestModel>,
    private val getTaxesUseCase: BaseSingleUseCase<PagedList<TaxModel>, GetTaxRequestModel>,
    private val getProductsUseCase: BaseSingleUseCase<PagedList<ProductModel>, GetProductRequestModel>,
    private val getMerchantsUseCase: BaseSingleUseCase<PagedList<MerchantModel>, GetMerchantRequestModel>,
    private val postNewSaleStockUseCase: BaseSingleUseCase<SaleModel, SaleRequestModel>,
    private val prefs: PrefsManager,
    private val dateFormatter: DateFormatter,
    private val database: RamaniDatabase
) : BaseViewModel(application, stringProvider, sessionManager) {

    var userId = ""
    var companyId = ""
    var currency = ""

    val dailySalesStatsActionLiveData = MutableLiveData<List<DailySalesStatsModel>>()
    val onDateChangedLiveData = SingleLiveEvent<String>()
    val onDataSyncCompletedLiveData = SingleLiveEvent<String>()

    val merchantList = mutableListOf<MerchantModel>()
    private var merchantPage = 1
    var onMerchantsLoaded = false

    val productList = mutableListOf<ProductModel>()
    private var productPage = 1
    var onProductsLoaded = false

    val taxesList = mutableListOf<TaxModel>()
    private var taxPage = 1
    var onTaxesLoaded = false

    private lateinit var flow: HomeFlow

    var calendar: Calendar = Calendar.getInstance()
    private var date = Date()

    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            userId = it.uuid
            companyId = it.companyId
            currency = prefs.currency
        }
    }

    private fun getUserAccountDetails() {
        val single = getUserAccountDetailsUseCase.getSingle(GetUserAccountDetailsRequestModel(companyId))
        subscribeSingle(single, onSuccess = {
            // Save it to pref
            if (it.isNotEmpty())
                prefs.userAccountDetails = it.first()

        }, onError = {
            //notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
        })
    }

    fun getTaxInformationForUser() {
        val single = getTaxInformationUseCase.getSingle(GetTaxInformationRequestModel(userId))
        subscribeSingle(single, onSuccess = {
            // Save it to pref
            prefs.taxInformation = it
        }, onError = {
            //notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
        })
    }

    private fun getDailySalesStats(startDate: String, endDate: String) {
        isLoadingVisible = true

        val single = dailySalesStatsUseCase.getSingle(DailySalesStatsRequestModel(companyId, userId, 1, startDate, endDate))
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

    fun syncData() {
        if (MAIN_SHARED_MODEL.isSynching)
            return

        val now = dateFormatter.getCalendarTimeWithDashesFull(Date())

        // Get last sync data
        val lastSyncTime = prefs.lastSyncTime

        merchantPage = 1
        taxPage = 1
        productPage = 1

        MAIN_SHARED_MODEL.isSynching = true

        isLoadingVisible = true

        onMerchantsLoaded = false
        onProductsLoaded = false
        onTaxesLoaded = false

        getMerchants(lastSyncTime, now)
        getProducts(lastSyncTime, now)
        getTaxes(now)
    }

    @SuppressLint("CheckResult")
    fun getMerchants(startDate: String, endDate: String) {
        isLoadingVisible = true

        if (merchantPage == 1) {
            merchantList.clear()
        }

        sessionManager.getLoggedInUser().subscribeBy {
            val single = getMerchantsUseCase.getSingle(GetMerchantRequestModel(true, companyId, startDate, endDate, true, merchantPage))
            subscribeSingle(single, onSuccess = {
                merchantList.addAll(it.data)

                if (it.paginationMeta.hasNext) {
                    merchantPage++
                    getMerchants(startDate, endDate)
                } else {
                    onMerchantsLoaded = true
                    checkSyncStatus(endDate)
                }
            }, onError = {
                onMerchantsLoaded = true
                checkSyncStatus(endDate)
            })
        }
    }

    @SuppressLint("CheckResult")
    fun getProducts(startDate: String, endDate: String) {
        isLoadingVisible = true

        if (productPage == 1) {
            productList.clear()
        }

        sessionManager.getLoggedInUser().subscribeBy {
            val single = getProductsUseCase.getSingle(GetProductRequestModel(true, companyId, startDate, endDate,false, productPage))
            subscribeSingle(single, onSuccess = {
                productList.addAll(it.data)
                if (it.paginationMeta.hasNext) {
                    productPage++
                    getProducts(startDate, endDate)
                } else {
                    onProductsLoaded = true
                    checkSyncStatus(endDate)
                }

            }, onError = {
                onProductsLoaded = true
                checkSyncStatus(endDate)
            })
        }
    }

    @SuppressLint("CheckResult")
    fun getTaxes(date: String) {
        isLoadingVisible = true

        if (taxPage == 1) {
            taxesList.clear()
        }

        sessionManager.getLoggedInUser().subscribeBy {
            val single = getTaxesUseCase.getSingle(GetTaxRequestModel(true, companyId, userId, date, taxPage))
            subscribeSingle(single, onSuccess = {
                taxesList.addAll(it.data)

                if (it.paginationMeta.hasNext) {
                    taxPage++
                    getTaxes(date)
                } else {
                    onTaxesLoaded = true
                    checkSyncStatus(date)
                }
            }, onError = {
                onTaxesLoaded = true
                checkSyncStatus(date)
            })
        }
    }

    private fun checkSyncStatus(datetime: String) {
        val allCompleted = onMerchantsLoaded && onTaxesLoaded && onProductsLoaded

        if (allCompleted) {
            prefs.lastSyncTime = datetime
            isLoadingVisible = false
            MAIN_SHARED_MODEL.isSynching = false
            onDataSyncCompletedLiveData.postValue(datetime)
        }

    }

    fun updateDate(year: Int?, monthOfYear: Int?, dayOfMonth: Int?) {
        year?.let {calendar.set(Calendar.YEAR, year) }
        monthOfYear?.let {calendar.set(Calendar.MONTH, monthOfYear) }
        dayOfMonth?.let {calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth) }

        date = calendar.time

        onDateChangedLiveData.postValue(dateFormatter.getCalendarTimeString(date))

        getUserAccountDetails()
        getTaxInformationForUser()
        getDailySalesStats()
    }

    private fun getDailySalesStats() {
        val dateString = dateFormatter.getCalendarTimeWithDashes(date)
        getDailySalesStats(
            dateString + "T00:00:00",
            dateString + "T23:59:59"
        )
    }

    fun getFormattedAmount(amount: Double): String = NumberFormat.getNumberInstance(Locale.US).format(amount)

    fun getFormattedAmount(amount: Int): String = NumberFormat.getNumberInstance(Locale.US).format(amount)
    fun getFormattedAmountLong(amount: Double): String = NumberFormat.getNumberInstance(Locale.US).format(amount)

    // Sent unsent sales
    fun tryToUnsentSale() {
        try {
            val saleJson = database.getSaleDao().getUnsentSale()

            if (TextUtils.isEmpty(saleJson.request))
                return

            val sale = Gson().fromJson(saleJson.request, SaleRequestModel::class.java)

            val single = postNewSaleStockUseCase.getSingle(sale)
            subscribeSingle(single, onSuccess = {
                database.getSaleDao().delete(saleJson)

                tryToUnsentSale()
            }, onError = {
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
            })
        } catch (e: Exception) {
            // In this case, we assume there is no unsent sales
            e.printStackTrace()
        }
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getUserAccountDetailsUseCase: BaseSingleUseCase<List<UserAccountDetailsModel>, GetUserAccountDetailsRequestModel>,
        private val getTaxInformationUseCase: BaseSingleUseCase<TaxInformationModel, GetTaxInformationRequestModel>,
        private val dailySalesStatsUseCase: BaseSingleUseCase<PagedList<DailySalesStatsModel>, DailySalesStatsRequestModel>,
        private val getTaxesUseCase: BaseSingleUseCase<PagedList<TaxModel>, GetTaxRequestModel>,
        private val getProductsUseCase: BaseSingleUseCase<PagedList<ProductModel>, GetProductRequestModel>,
        private val getMerchantsUseCase: BaseSingleUseCase<PagedList<MerchantModel>, GetMerchantRequestModel>,
        private val postNewSaleStockUseCase: BaseSingleUseCase<SaleModel, SaleRequestModel>,
        private val prefs: PrefsManager,
        private val dateFormatter: DateFormatter,
        private val database: RamaniDatabase
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    getUserAccountDetailsUseCase,
                    getTaxInformationUseCase,
                    dailySalesStatsUseCase, getTaxesUseCase, getProductsUseCase, getMerchantsUseCase,
                    postNewSaleStockUseCase,
                    prefs,
                    dateFormatter,
                    database
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}