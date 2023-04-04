package io.ramani.ramaniStationary.app.history.presentation

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.common.presentation.errors.PresentationError
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.data.history.models.request.GetHistoryRequestModel
import io.ramani.ramaniStationary.data.history.models.request.GetOrderDetailsRequestModel
import io.ramani.ramaniStationary.data.history.models.request.GetXReportRequestModel
import io.ramani.ramaniStationary.data.history.models.request.GetZReportRequestModel
import io.ramani.ramaniStationary.data.history.models.response.Activity
import io.ramani.ramaniStationary.data.history.models.response.HistoryResponse
import io.ramani.ramaniStationary.data.history.models.response.OrderDetailsResponse
import io.ramani.ramaniStationary.data.history.models.response.Summary
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
    private val prefs: PrefsManager,
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

    override fun start(args: Map<String, Any?>) {
        isThereTaxObject()
        val split = today().split("/")
        getHistory(
            split.get(0).toInt(),
            monthsArray.get(split.get(1).toInt() - 1),
            split.get(2).toInt()
        )
        getOrderDetails("lfjcmnm6")
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
        sessionManager.getTaxObject().subscribeBy {
            if (it.isNotNull()){
                isThereTaxObject.postValue(true)
            }else{
                isThereTaxObject.postValue(false)
            }
        }
    }

    fun getHistory(day: Int, month: String, year: Int) {
        isLoadingVisible = true
        sessionManager.getLoggedInUser().subscribeBy {
            val single =
                getHistoryUseCase.getSingle(GetHistoryRequestModel(it.uuid, day, month, year))
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false
                historyActivityListOriginal.addAll(it?.activities!!)
                historyActivityLiveData.postValue(it?.activities!!)
                historySummaryLiveData.postValue(it?.summary!!)
                Log.d("History Response", it?.activities.toString())
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
            Log.d("History Response", it?.get(0).toString())
        }, onError = {
            isLoadingVisible = false
        })
    }

    fun getXReport(date: String) {
        isLoadingVisible = true
        sessionManager.getLoggedInUser().subscribeBy { userModel ->
            sessionManager.getTaxObject().subscribeBy { taxInformation ->
                val single =
                    getXReportUseCase.getSingle(
                        GetXReportRequestModel(
                            taxInformation.UIN,
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
    }

    fun getZreportByRange(
        startDate: String,
        endDate: String
    ) {
        isLoadingVisible = true
        sessionManager.getLoggedInUser().subscribeBy { userModel ->
            sessionManager.getTaxObject().subscribeBy { taxInformation ->
                val single =
                    getZreportByRangeUseCase.getSingle(
                        GetZReportRequestModel(
                            taxInformation.UIN,
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
    }



    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getHistoryUseCase: BaseSingleUseCase<HistoryResponse?, GetHistoryRequestModel>,
        private val getZreportByRangeUseCase: BaseSingleUseCase<String?, GetZReportRequestModel>,
        private val getXReportUseCase: BaseSingleUseCase<String?, GetXReportRequestModel>,
        private val getOrderDetailsUseCase: BaseSingleUseCase<List<OrderDetailsResponse>?, GetOrderDetailsRequestModel>,
        private val prefs: PrefsManager,
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
                    prefs,
                    printerHelper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }

    }
}
