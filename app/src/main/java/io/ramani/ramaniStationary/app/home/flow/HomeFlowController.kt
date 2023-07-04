package io.ramani.ramaniStationary.app.home.flow

import io.ramani.ramaniStationary.app.auth.presentation.LoginFragment
import io.ramani.ramaniStationary.app.common.extensions.letIfType
import io.ramani.ramaniStationary.app.common.navgiation.NavigationManager
import io.ramani.ramaniStationary.app.common.navgiation.NavigationTags
import io.ramani.ramaniStationary.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniStationary.app.common.presentation.dialogs.BaseNavigationViewInterface
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.createmerchant.presentation.CreateMerchantFragment
import io.ramani.ramaniStationary.app.createorder.presentation.CreateOrderFragment
import io.ramani.ramaniStationary.app.credit.presentation.CreditFragment
import io.ramani.ramaniStationary.app.history.presentation.HistoryFragment
import io.ramani.ramaniStationary.app.home.presentation.HomeFragment
import io.ramani.ramaniStationary.app.orderdetails.presentation.OrderDetailsFragment
import io.ramani.ramaniStationary.app.reports.presentation.ReportsFragment
import io.ramani.ramaniStationary.app.stock.presentation.StockFragment

class HomeFlowController(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : HomeFlow {

    private var navigationManager: NavigationManager? = null
    private var hideBackOnRoot = true
    private var isBackEnabled = false

    init {
        navigationManager = NavigationManager().apply {
            init(activity.supportFragmentManager, mainFragmentContainer)
        }

        navigationManager?.setOnBackStackChangedListener(object :
            NavigationManager.NavigationListener {
                override fun onBackStackChanged() {
                    if (hideBackOnRoot) {
                        isBackEnabled = checkBackEnabled()
                    }
                }
            }
        )
    }

    override fun openLogin() {
        val fragment = LoginFragment.newInstance()
        activity.navigationManager?.openAsRoot(
            fragment
        )
    }

    override fun openHome() {
        val fragment = HomeFragment.newInstance()
        navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.REPLACE,
            needAnimation = false
        )
    }

    override fun openStock() {
        val fragment = StockFragment.newInstance()
        navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.REPLACE,
            needAnimation = false
        )
    }

    override fun openHistory() {
        val fragment = HistoryFragment.newInstance()
        navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.REPLACE,
            needAnimation = false
        )
    }

    override fun openCredit() {
        val fragment = CreditFragment.newInstance()
        navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.REPLACE,
            needAnimation = false
        )
    }

    override fun openAllTodaySales() {
        val fragment = ReportsFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openAllCustomers() {
        val fragment = ReportsFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openCreateNewOrder() {
        val fragment = CreateOrderFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openSalesReports() {
        val fragment = ReportsFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openCreateMerchant() {
        val fragment = CreateMerchantFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun onBackPressed() {
        if (navigationManager?.topFragment() is BaseFragment) {
            if (!(navigationManager?.topFragment() as BaseFragment).onBackButtonPressed()) {
                navigationBack()
            }
        } else {
            navigationBack()
        }
    }

    override fun openOrderDetails(orderID: String) {
        val fragment = OrderDetailsFragment.newInstance(orderID)
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    protected fun navigationBack() {
        navigationManager?.navigateBackStack(activity)
    }

    private fun checkBackEnabled() =
        navigationManager?.let { manager ->
            manager.topFragment()?.letIfType<BaseNavigationViewInterface, Boolean> {
                when (it.navTag) {
                    NavigationTags.HOME,
                    NavigationTags.FIND_US,
                    NavigationTags.BOOKING -> false
                    else -> true
                }
            } ?: true
        } ?: true
}