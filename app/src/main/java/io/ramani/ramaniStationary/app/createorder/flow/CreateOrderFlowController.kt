package io.ramani.ramaniStationary.app.createorder.flow

import io.ramani.ramaniStationary.app.auth.presentation.LoginFragment
import io.ramani.ramaniStationary.app.common.presentation.actvities.BaseActivity

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
        // TODO("Not yet implemented")
    }
}