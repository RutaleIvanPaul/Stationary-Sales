package io.ramani.ramaniStationary.app.auth.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.auth.flow.AuthFlow
import io.ramani.ramaniStationary.app.auth.flow.AuthFlowController
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signin_sheet.*
import org.kodein.di.generic.factory

class LoginFragment : BaseFragment() {
    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModelProvider: (Fragment) -> LoginViewModel by factory()
    private lateinit var viewModel: LoginViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: AuthFlow

    override fun getLayoutResId(): Int = R.layout.fragment_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    override fun initView(view: View?) {
        super.initView(view)
        flow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)
        sign_in_button.setOnSingleClickListener {
            flow.openSigninSheet()
        }

    }
}