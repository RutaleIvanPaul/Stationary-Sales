package io.ramani.ramaniStationary.app.createorder.flow

import io.ramani.ramaniStationary.app.auth.presentation.LoginFragment
import io.ramani.ramaniStationary.app.common.navgiation.NavigationManager
import io.ramani.ramaniStationary.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniStationary.app.createorder.presentation.CheckoutFragment
import io.ramani.ramaniStationary.app.createorder.presentation.CreateOrderFragment

class CreateOrderFlowController(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : CreateOrderFlow {

    override fun openAddItems() {
        val fragment = LoginFragment.newInstance()
        activity.navigationManager?.openAsRoot(
            fragment
        )
    }

    override fun openCheckout() {
        val fragment = CheckoutFragment.newInstance()
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }
}