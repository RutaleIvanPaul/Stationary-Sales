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
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_bottom_nav.*
import org.kodein.di.generic.factory
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

    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
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
                viewModel.calendar.get(Calendar.YEAR),
                viewModel.calendar.get(Calendar.MONTH),
                viewModel.calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        initSubscribers()
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
        viewModel.onDateChangedLiveData.observe(this) {
            home_select_date.text = it
        }

        viewModel.dailySalesStatsActionLiveData.observe(this) {
            if (it.isNotEmpty()) {
                home_total_sales_tv.text = String.format("TSH %s", viewModel.getFormattedAmount(it.first().totalSales))
                home_total_customers_tv.text = viewModel.getFormattedAmount(it.first().totalNumberOfCustomers)
            }
        }

        viewModel.onDataSyncCompletedLiveData.observe(this) {
            home_last_updated_at.text = String.format("%s %s", getString(R.string.last_updated_at), it)
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
            if (canNavigate())
                flow.openAllTodaySales()    // View all today sales
        }

        home_total_customers_all_view.setOnSingleClickListener {
            if (canNavigate())
                flow.openAllCustomers() // View all customers
        }

        home_create_new_order.setOnSingleClickListener {
            if (canNavigate())
                flow.openCreateNewOrder()   // Create new order
        }

        home_sales_report.setOnSingleClickListener {
            if (canNavigate())
                flow.openSalesReports()     // Sales report
        }

        home_create_merchant.setOnSingleClickListener {
            if (canNavigate())
                flow.openCreateMerchant()   // Create merchant
        }
    }

    private fun setupBottomTab() {
        home_tab_refresh.setOnSingleClickListener {
            if (canNavigate())
                viewModel.syncData()
        }

        home_nav_stock.setOnSingleClickListener {
            if (canNavigate())
                flow.openStock()
        }

        home_nav_history.setOnSingleClickListener {
            if (canNavigate())
                flow.openHistory()
        }

        home_nav_credit.setOnSingleClickListener {
            if (canNavigate())
                flow.openCredit()
        }
    }

    private fun canNavigate() =
        if (viewModel.isInSync) {
            errorDialog("Data sync is being done. Please wait until it's finished.")
            false
        } else {
            true
        }

    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            viewModel.updateDate(year, monthOfYear, dayOfMonth)
        }
}