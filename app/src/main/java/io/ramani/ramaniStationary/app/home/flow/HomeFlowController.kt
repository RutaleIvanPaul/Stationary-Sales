package io.ramani.ramaniStationary.app.home.flow

import io.ramani.ramaniStationary.app.auth.presentation.LoginFragment
import io.ramani.ramaniStationary.app.auth.presentation.SigninBottomSheetFragment
import io.ramani.ramaniStationary.app.common.presentation.actvities.BaseActivity

class HomeFlowController(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : HomeFlow {

    override fun openLogin() {
        val fragment = LoginFragment.newInstance()
        activity.navigationManager?.openAsRoot(
            fragment
        )
    }

    override fun openAllTodaySales() {
        TODO("Not yet implemented")
    }

    override fun openAllCustomers() {
        TODO("Not yet implemented")
    }

    override fun openCreateNewOrder() {
        TODO("Not yet implemented")
    }

    override fun openSalesReports() {
        TODO("Not yet implemented")
    }

    override fun openCreateMerchant() {
        TODO("Not yet implemented")
    }

    override fun openStock() {
        TODO("Not yet implemented")
    }

    override fun openHistory() {
        TODO("Not yet implemented")
    }

    override fun openCredit() {
        TODO("Not yet implemented")
    }

}