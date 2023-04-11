package io.ramani.ramaniStationary.app.history.presentation

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.common.presentation.errors.PresentationError
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.data.history.models.request.*
import io.ramani.ramaniStationary.data.history.models.response.*
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domainCore.date.*
import io.ramani.ramaniStationary.domainCore.lang.isNotNull
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import io.ramani.ramaniStationary.domainCore.printer.PrinterHelper
import io.reactivex.rxkotlin.subscribeBy

class HistoryViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val getHistoryUseCase: BaseSingleUseCase<HistoryResponse?, GetHistoryRequestModel>,
    private val getZreportByRangeUseCase: BaseSingleUseCase<String?, GetZReportRequestModel>,
    private val getXReportUseCase: BaseSingleUseCase<String?, GetXReportRequestModel>,
    private val getOrderDetailsUseCase: BaseSingleUseCase<List<OrderDetailsResponse>?, GetOrderDetailsRequestModel>,
    private val getReceiptUseCase: BaseSingleUseCase<TRAReceipt, PrintReceiptRequest>,
    val prefs: PrefsManager,
    private val printerHelper: PrinterHelper
) : BaseViewModel(application, stringProvider, sessionManager) {

    val monthsArray = arrayOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    var currency = prefs.currency

    val historyActivityLiveData = MutableLiveData<List<Activity>>()
    val historySummaryLiveData = MutableLiveData<Summary>()
    val isThereTaxObject = MutableLiveData<Boolean>()
    val historyActivityListOriginal = mutableListOf<Activity>()

    val orderDetailsLiveData = MutableLiveData<OrderDetailsResponse>()

    override fun start(args: Map<String, Any?>) {
        isThereTaxObject()
    }


    fun printText(receiptText:String){
        val printText = printerHelper.printText(receiptText)
        if(!printText.status){
            notifyErrorObserver(printText.error, PresentationError.ERROR_TEXT)
        }
    }

    fun printXreport(){
        getXReport(todayHyphenated())
    }

    fun printZreportToday(){
        getZreportByRange(todayHyphenated(), todayHyphenated())
    }

    fun printZreportYesterday() {
        getZreportByRange(getDateYesterDay(), getDateYesterDay())
    }

    fun printZreportLastMonth(){
        getZreportByRange(getDateLastMonthStart(), getDateLastMonthEnd())
    }

    fun isThereTaxObject(){
        isThereTaxObject.postValue(prefs.taxInformation.isNotNull())
    }

    @SuppressLint("CheckResult")
    fun getHistory(day: Int, month: String, year: Int) {
        isLoadingVisible = true
        sessionManager.getLoggedInUser().subscribeBy {
            val single =
                getHistoryUseCase.getSingle(GetHistoryRequestModel(it.uuid, day, month, year))
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false
                historyActivityListOriginal.addAll(it?.activities!!)
                historyActivityLiveData.postValue(it.activities!!)
                historySummaryLiveData.postValue(it.summary!!)
            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
            })
        }
    }

    fun getOrderDetails(orderId: String) {
        isLoadingVisible = true
        val single =
            getOrderDetailsUseCase.getSingle(GetOrderDetailsRequestModel(orderId))
        subscribeSingle(single, onSuccess = {
            isLoadingVisible = false
            orderDetailsLiveData.postValue(it!![0])
        }, onError = {
            isLoadingVisible = false
            notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
        })
    }

    @SuppressLint("CheckResult")
    fun getXReport(date: String) {
        isLoadingVisible = true
        sessionManager.getLoggedInUser().subscribeBy { userModel ->
            val single =
                getXReportUseCase.getSingle(
                    GetXReportRequestModel(
                        prefs.taxInformation.uin,
                        date,
                        userModel.name,
                        userModel.companyId
                    )
                )
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false
                printText(it.toString())
            }, onError = {
                isLoadingVisible = false
            })
        }
    }

    @SuppressLint("CheckResult")
    fun getZreportByRange(
        startDate: String,
        endDate: String
    ) {
        isLoadingVisible = true
        sessionManager.getLoggedInUser().subscribeBy { userModel ->
            val single =
                getZreportByRangeUseCase.getSingle(
                    GetZReportRequestModel(
                        prefs.taxInformation.uin,
                        userModel.companyId,
                        startDate,
                        endDate,
                        userModel.name
                    )
                )
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false
                printText(it.toString())
            }, onError = {
                isLoadingVisible = false
            })
        }
    }

    @SuppressLint("CheckResult")
    fun getAndPrintReceipt(orderId: String){
        isLoadingVisible = true
        sessionManager.getLoggedInUser().subscribeBy { userModel ->
            val single = getReceiptUseCase.getSingle(
                PrintReceiptRequest(orderId, prefs.taxInformation.uin, userModel.name)
            )

            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false
                printText(it.receiptText)
            }, onError = {
                isLoadingVisible = false
            })
        }
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getHistoryUseCase: BaseSingleUseCase<HistoryResponse?, GetHistoryRequestModel>,
        private val getZreportByRangeUseCase: BaseSingleUseCase<String?, GetZReportRequestModel>,
        private val getXReportUseCase: BaseSingleUseCase<String?, GetXReportRequestModel>,
        private val getOrderDetailsUseCase: BaseSingleUseCase<List<OrderDetailsResponse>?, GetOrderDetailsRequestModel>,
        private val getReceiptUseCase: BaseSingleUseCase<TRAReceipt, PrintReceiptRequest>,
        val prefs: PrefsManager,
        private val printerHelper: PrinterHelper
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                return HistoryViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    getHistoryUseCase,
                    getZreportByRangeUseCase,
                    getXReportUseCase,
                    getOrderDetailsUseCase,
                    getReceiptUseCase,
                    prefs,
                    printerHelper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }

    }
}
