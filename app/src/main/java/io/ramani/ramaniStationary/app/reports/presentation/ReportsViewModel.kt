package io.ramani.ramaniStationary.app.reports.presentation

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.ramani.ramaniStationary.app.common.presentation.errors.PresentationError
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.data.createmerchant.models.request.GetTopPerformersRequestModel
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domain.base.SingleLiveEvent
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createmerchant.model.NameValueModel
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import io.ramani.ramaniStationary.domain.datetime.DateFormatter
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
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
    private val prefs: PrefsManager,
    private val dateFormatter: DateFormatter,
    private val printerHelper: PrinterHelper
) : BaseViewModel(application, stringProvider, sessionManager) {

    var userModel: UserModel = UserModel()
    var userId = ""
    var companyId = ""

    val topSalePeoples = mutableListOf<NameValueModel>()
    val topMerchants = mutableListOf<NameValueModel>()
    val onTopPerformersLoadedLiveData = SingleLiveEvent<TopPerformersModel>()
    var totalSales: String = "0"

    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            userModel = it
            userId = it.uuid
            companyId = it.companyId

            getTopPerformers(null, null)
        }
    }

    @SuppressLint("CheckResult")
    fun getTopPerformers(startDate: Date?, endDate: Date?) {
        isLoadingVisible = true

        totalSales = "0"
        topSalePeoples.clear()
        topMerchants.clear()

        val today = Date()

        val startDateStr = if (startDate != null) getStartDateString(startDate) else getStartDateString(today)
        val endDateStr = if (endDate != null) getEndDateString(endDate) else getEndDateString(today)

        sessionManager.getLoggedInUser().subscribeBy { user ->
            val single = getTopPerformersUseCase.getSingle(GetTopPerformersRequestModel(user.companyId, startDateStr, endDateStr, 1000))
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false

                topSalePeoples.addAll(it.topSalesPeople)
                topMerchants.addAll(it.topMerchants)

                var total = 0
                topSalePeoples.forEach { nv ->
                    total += nv.value.toInt()
                }

                totalSales = getFormattedAmount(total)

                onTopPerformersLoadedLiveData.postValue(it)
            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
            })
        }
    }

    private fun getFormattedAmount(amount: Int): String = NumberFormat.getNumberInstance(Locale.US).format(amount)
    private fun getStartDateString(date: Date) = dateFormatter.getTimeWithFormmatter(date, "yyyy-MM-dd'T'00:00:00.000'Z'")
    private fun getEndDateString(date: Date) = dateFormatter.getTimeWithFormmatter(date, "yyyy-MM-dd'T'23:59:59.000'Z'")

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getTopPerformersUseCase: BaseSingleUseCase<TopPerformersModel, GetTopPerformersRequestModel>,
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
                    prefs,
                    dateFormatter,
                    printerHelper
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}