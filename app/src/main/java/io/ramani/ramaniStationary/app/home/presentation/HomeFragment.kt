package io.ramani.ramaniStationary.app.home.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
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
import io.ramani.ramaniStationary.app.createorder.presentation.CREATE_ORDER_MODEL
import io.ramani.ramaniStationary.app.home.flow.HomeFlow
import io.ramani.ramaniStationary.app.home.flow.HomeFlowController
import io.ramani.ramaniStationary.app.main.presentation.MAIN_SHARED_MODEL
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.generic.factory
import java.util.*

class HomeFragment : BaseFragment(), NavigationView.OnNavigationItemSelectedListener {
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

        viewModel.updateDate(null, null, null)
    }

    override fun onResume() {
        super.onResume()

        CREATE_ORDER_MODEL.clear()

        if (isAdded && MAIN_SHARED_MODEL.isOnline)
            viewModel.tryToUnsentSale()
    }

    private fun subscribeResponse() {
        viewModel.onDateChangedLiveData.observe(this) {
            home_select_date.text = it
        }

        viewModel.dailySalesStatsActionLiveData.observe(this) {
            if (it.isNotEmpty()) {
                home_total_sales_tv.text = String.format("%s %s", viewModel.currency, viewModel.getFormattedAmountLong(it.first().totalSales))
                home_total_customers_tv.text = viewModel.getFormattedAmount(it.first().totalNumberOfCustomers)
            } else {
                home_total_sales_tv.text = String.format("%s 0", viewModel.currency)
                home_total_customers_tv.text = "0"
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

        if (MAIN_SHARED_MODEL.isOnline)
            errorDialog(error)
    }

    private fun setupMenu() {
        navigation_view_side_menu.setNavigationItemSelectedListener(this)
        home_menu.setOnSingleClickListener {
            side_menu_drawer.openDrawer(Gravity.LEFT)
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

    private fun canNavigate() =
        if (MAIN_SHARED_MODEL.isSynching) {
            errorDialog("Data sync is being done. Please wait until it's finished.")
            false
        } else {
            true
        }

    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            viewModel.updateDate(year, monthOfYear, dayOfMonth)
        }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        side_menu_drawer.closeDrawer(Gravity.LEFT)
        when(item.itemId) {
            R.id.nav_log_out -> {
                showConfirmDialog(getString(R.string.confirm_logout), onConfirmed = {
                    viewModel.logout {
                        authFlow.openLogin()
                    }
                })
        }
            R.id.nav_sync_data -> {
                viewModel.syncData()
            }
        }
        return false
    }

}