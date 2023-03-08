package io.ramani.ramaniStationary.app.home.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.auth.flow.AuthFlow
import io.ramani.ramaniStationary.app.auth.flow.AuthFlowController
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.dialogs.showConfirmDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.showSelectPopUp
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.home.flow.HomeFlow
import io.ramani.ramaniStationary.app.home.flow.HomeFlowController
import io.ramani.ramaniStationary.domain.datetime.DateFormatter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_bottom_nav.*
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import java.text.NumberFormat
import java.util.*

class HomeFragment : BaseFragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModelProvider: (Fragment) -> HomeViewModel by factory()
    private lateinit var viewModel: HomeViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: HomeFlow
    private lateinit var authFlow: AuthFlow

    private val dateFormatter: DateFormatter by instance()
    private var calendar = Calendar.getInstance()
    private var date = Date()

    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
        initSubscribers()
    }

    override fun initView(view: View?) {
        super.initView(view)

        flow = HomeFlowController(baseActivity!!, R.id.main_fragment_container)
        authFlow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)

        setupMenu()
        setupNav()
        setupBottomTab()

        home_select_date.setOnSingleClickListener {
            DatePickerDialog(
                requireActivity(),
                startDateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        updateDate()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeResponse()
        viewModel.start()
    }

    private fun subscribeResponse() {
        viewModel.dailySalesStatsActionLiveData.observe(this) {
            home_total_sales_tv.text = String.format("TSH %s", getFormattedAmount(it.first().totalSales))
            home_total_customers_tv.text = getFormattedAmount(it.first().totalNumberOfCustomers)
        }

        viewModel.onDataSyncCompletedLiveData.observe(this) {

        }
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        home_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun setupMenu() {
        home_menu.setOnSingleClickListener {
            home_menu.showSelectPopUp(listOf(getString(R.string.logout)),
                wrapWidth = true,
                onItemClick = { _, _, _ ->
                    showConfirmDialog(getString(R.string.confirm_logout), onConfirmed = {
                        viewModel.logout {
                            authFlow.openLogin()
                        }
                    })
                })
        }
    }

    private fun setupNav() {
        home_today_sales_all_view.setOnSingleClickListener {
            // View all today sales
            flow.openAllTodaySales()
        }

        home_total_customers_all_view.setOnSingleClickListener {
            // View all customers
            flow.openAllCustomers()
        }

        home_create_new_order.setOnSingleClickListener {
            // Create new order
            flow.openCreateNewOrder()
        }

        home_sales_report.setOnSingleClickListener {
            // Sales report
            flow.openSalesReports()
        }

        home_create_merchant.setOnSingleClickListener {
            // Create merchant
            flow.openCreateMerchant()
        }
    }

    private fun setupBottomTab() {
        home_tab_refresh.setOnSingleClickListener {
            doSyncData()
        }

        home_nav_stock.setOnSingleClickListener {
            flow.openStock()
        }

        home_nav_history.setOnSingleClickListener {
            flow.openHistory()
        }

        home_nav_credit.setOnSingleClickListener {
            flow.openCredit()
        }
    }

    private fun updateDate() {
        date = calendar.time

        home_select_date.text = dateFormatter.getCalendarTimeString(date)
        getDailySalesStats()
    }

    private fun doSyncData() {
        viewModel.syncData(dateFormatter.getCalendarTimeWithDashesFull(Date()))
    }

    private fun getDailySalesStats() {
        val dateString = dateFormatter.getCalendarTimeWithDashes(Date())
        viewModel.getDailySalesStats(
            dateString + "T00:00:00",
            dateString + "T23:59:59"
        )
    }

    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate()
        }

    private fun getFormattedAmount(amount: Int): String = NumberFormat.getNumberInstance(Locale.US).format(amount)
}