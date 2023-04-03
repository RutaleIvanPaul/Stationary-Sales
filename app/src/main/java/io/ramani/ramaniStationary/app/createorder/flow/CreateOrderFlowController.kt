package io.ramani.ramaniStationary.app.createorder.flow

import io.ramani.ramaniStationary.app.common.navgiation.NavigationManager
import io.ramani.ramaniStationary.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniStationary.app.createorder.presentation.CheckoutFragment
import io.ramani.ramaniStationary.app.createorder.presentation.CreateOrderFragment
import io.ramani.ramaniStationary.app.createorder.presentation.OrderCompletedFragment
import io.ramani.ramaniStationary.app.createorder.presentation.PrintSuccessfulFragment

class CreateOrderFlowController(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : CreateOrderFlow {

    override fun openAddItems() {
        val fragment = CreateOrderFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.REPLACE
        )
    }

    override fun openCheckout() {
        val fragment = CheckoutFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openOrderCompleted() {
        val fragment = OrderCompletedFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

    override fun openPrintSuccessful() {
        val fragment = PrintSuccessfulFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }
}