package io.ramani.ramaniStationary.app.reports.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.common.presentation.errors.PresentationError
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.data.createmerchant.models.request.GetTopPerformersRequestModel
import io.ramani.ramaniStationary.data.reports.models.request.GetSalesSummaryStatisticsRequestModel
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domain.base.SingleLiveEvent
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createmerchant.model.NameValueModel
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.ramani.ramaniStationary.domain.datetime.DateFormatter
import io.ramani.ramaniStationary.domain.reports.model.SalesSummaryStatisticsModel
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import io.ramani.ramaniStationary.domainCore.printer.PrintStatus
import io.ramani.ramaniStationary.domainCore.printer.PrinterHelper
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.NumberFormat
import java.util.*

class ReportsViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val getTopPerformersUseCase: BaseSingleUseCase<TopPerformersModel, GetTopPerformersRequestModel>,
    private val getSalesSummaryStatisticsUseCase: BaseSingleUseCase<SalesSummaryStatisticsModel, GetSalesSummaryStatisticsRequestModel>,
    private val prefs: PrefsManager,
    private val dateFormatter: DateFormatter,
    private val printerHelper: PrinterHelper
) : BaseViewModel(application, stringProvider, sessionManager) {

    var userModel: UserModel = UserModel()
    var userId = ""
    var companyId = ""
    var currency = ""

    val topSalePeoples = mutableListOf<NameValueModel>()
    val topMerchants = mutableListOf<NameValueModel>()
    val topProducts = mutableListOf<NameValueModel>()
    var salesSummary: SalesSummaryStatisticsModel = SalesSummaryStatisticsModel()

    val onTopPerformersLoadedLiveData = SingleLiveEvent<TopPerformersModel>()
    val onSalesSummaryLoadedLiveData = SingleLiveEvent<SalesSummaryStatisticsModel>()

    var startDate = Date()
    var endDate = Date()
    var dayRange = ReportsFragment.DayRange.TODAY
    var calendar = Calendar.getInstance()

    var onDateChangedLiveData = SingleLiveEvent<ReportsFragment.DayRange>()

    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            userModel = it
            userId = it.uuid
            companyId = it.companyId
            currency = prefs.currency

            updateDate(ReportsFragment.DayRange.TODAY)
        }
    }

    fun updateDate(dayRange: ReportsFragment.DayRange) {
        val today = Date()
        when (dayRange) {
            ReportsFragment.DayRange.TODAY -> {
                startDate = today
                endDate = today
            }
            ReportsFragment.DayRange.YESTERDAY -> {
                val yesterday = Date(System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS)
                startDate = yesterday
                endDate = yesterday
            }
            ReportsFragment.DayRange.LAST_7_DAYS -> {
                startDate = Date(System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS * 6)
                endDate = today
            }
            ReportsFragment.DayRange.LAST_30_DAYS -> {
                startDate = Date(System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS * 30)
                endDate = today
            }
            else -> {}
        }

        this.dayRange = dayRange
        onDateChangedLiveData.postValue(dayRange)

        getTopPerformers(startDate, endDate)
        getSalesSummary(startDate, endDate)
    }

    @SuppressLint("CheckResult")
    fun getTopPerformers(startDate: Date?, endDate: Date?) {
        isLoadingVisible = true

        topSalePeoples.clear()
        topMerchants.clear()
        topProducts.clear()

        val today = Date()

        val startDateStr = if (startDate != null) getStartDateString(startDate) else getStartDateString(today)
        val endDateStr = if (endDate != null) getEndDateString(endDate) else getEndDateString(today)

        sessionManager.getLoggedInUser().subscribeBy { user ->
            val single = getTopPerformersUseCase.getSingle(GetTopPerformersRequestModel(user.companyId, user.uuid, startDateStr, endDateStr, 1000))
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false

                topSalePeoples.addAll(it.topSalesPeople)
                topMerchants.addAll(it.topMerchants)
                topProducts.addAll(it.topProducts)

                onTopPerformersLoadedLiveData.postValue(it)
            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
            })
        }
    }

    @SuppressLint("CheckResult")
    fun getSalesSummary(startDate: Date?, endDate: Date?) {
        isLoadingVisible = true

        val today = Date()

        val startDateStr = if (startDate != null) getDateString(startDate) else getDateString(today)
        val endDateStr = if (endDate != null) getDateString(endDate) else getDateString(today)

        sessionManager.getLoggedInUser().subscribeBy { user ->
            val single = getSalesSummaryStatisticsUseCase.getSingle(
                GetSalesSummaryStatisticsRequestModel(user.companyId, user.uuid,1, startDateStr, endDateStr)
            )
            subscribeSingle(single, onSuccess = { summary ->
                isLoadingVisible = false

                salesSummary = summary

                onSalesSummaryLoadedLiveData.postValue(summary)
            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
            })
        }
    }

    fun printBitmap(bitmap: Bitmap): PrintStatus {
        val status = printerHelper.printBitmap(bitmap)
        if(!status.status){
            notifyErrorObserver(status.error, PresentationError.ERROR_TEXT)
        }

        return status
    }

    fun getFormattedAmount(amount: Double): String = NumberFormat.getNumberInstance(Locale.US).format(amount.toInt())
    private fun getStartDateString(date: Date) = dateFormatter.getTimeWithFormmatter(date, "yyyy-MM-dd'T'00:00:00.000'Z'")
    private fun getEndDateString(date: Date) = dateFormatter.getTimeWithFormmatter(date, "yyyy-MM-dd'T'23:59:59.000'Z'")

    private fun getDateString(date: Date) = dateFormatter.getTimeWithFormmatter(date, "yyyy-MM-dd")
    fun getDateRangeString() = String.format("%s - %s", dateFormatter.getTimeWithFormmatter(startDate, "dd MMM yyyy"), dateFormatter.getTimeWithFormmatter(endDate, "dd MMM yyyy"))

    fun isYesterday(d: Date): Boolean {
        return DateUtils.isToday(d.time + DateUtils.DAY_IN_MILLIS)
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getTopPerformersUseCase: BaseSingleUseCase<TopPerformersModel, GetTopPerformersRequestModel>,
        private val getSalesSummaryStatisticsUseCase: BaseSingleUseCase<SalesSummaryStatisticsModel, GetSalesSummaryStatisticsRequestModel>,
        private val prefs: PrefsManager,
        private val dateFormatter: DateFormatter,
        private val printerHelper: PrinterHelper
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReportsViewModel::class.java)) {
                return ReportsViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    getTopPerformersUseCase,
                    getSalesSummaryStatisticsUseCase,
                    prefs,
                    dateFormatter,
                    printerHelper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}