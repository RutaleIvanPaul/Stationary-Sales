package io.ramani.ramaniStationary.app.credit.flow

import io.ramani.ramaniStationary.app.common.navgiation.NavigationManager
import io.ramani.ramaniStationary.app.common.presentation.actvities.BaseActivity
import io.ramani.ramaniStationary.app.credit.presentation.CreditOrderDetailFragment

class CreditFlowController(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : CreditFlow {

    override fun openOrderDetails(orderID: String) {
        val fragment = CreditOrderDetailFragment.newInstance(orderID)
        activity.navigationManager?.open(
            fragment,
            openMethod = NavigationManager.OpenMethod.ADD
        )
    }

}