package io.ramani.ramaniStationary.app.auth.flow

import io.ramani.ramaniStationary.app.auth.presentation.LoginFragment
import io.ramani.ramaniStationary.app.auth.presentation.SigninBottomSheetFragment
import io.ramani.ramaniStationary.app.common.presentation.actvities.BaseActivity

class AuthFlowController(
    private val activity: BaseActivity,
    private val mainFragmentContainer: Int
) : AuthFlow {

    override fun openLogin() {
        val fragment = LoginFragment.newInstance()
        activity.navigationManager?.openAsRoot(
            fragment
        )
    }

    override fun openSigninSheet() {
        val fragment = SigninBottomSheetFragment()
        activity.supportFragmentManager?.let { fragment.show(it, "signin_sheet_fragment") }
    }

}