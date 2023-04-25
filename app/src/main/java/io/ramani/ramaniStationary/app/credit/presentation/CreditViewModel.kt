package io.ramani.ramaniStationary.app.credit.presentation

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
import io.ramani.ramaniStationary.data.credit.models.request.GetLocationsRequestModel
import io.ramani.ramaniStationary.data.database.RamaniDatabase
import io.ramani.ramaniStationary.data.home.models.request.*
import io.ramani.ramaniStationary.data.home.models.response.UserAccountDetailsRemoteModel
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.base.SingleLiveEvent
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createorder.model.SaleModel
import io.ramani.ramaniStationary.domain.credit.model.LocationModel
import io.ramani.ramaniStationary.domain.datetime.DateFormatter
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.model.*
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.generic.instance
import java.text.NumberFormat
import java.util.*

class CreditViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val getListLocationsUseCase: BaseSingleUseCase<PagedList<LocationModel>, GetLocationsRequestModel>,
    private val prefs: PrefsManager,
    private val dateFormatter: DateFormatter,
    private val database: RamaniDatabase
) : BaseViewModel(application, stringProvider, sessionManager) {

    var userId = ""
    var companyId = ""
    var currency = ""

    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            userId = it.uuid
            companyId = it.companyId
            currency = prefs.currency
        }
    }

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getListLocationsUseCase: BaseSingleUseCase<PagedList<LocationModel>, GetLocationsRequestModel>,
        private val prefs: PrefsManager,
        private val dateFormatter: DateFormatter,
        private val database: RamaniDatabase
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreditViewModel::class.java)) {
                return CreditViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    getListLocationsUseCase,
                    prefs,
                    dateFormatter,
                    database
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}