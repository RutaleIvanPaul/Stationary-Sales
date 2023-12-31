package io.ramani.ramaniStationary.app.auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.auth.flow.AuthFlow
import io.ramani.ramaniStationary.app.auth.flow.AuthFlowController
import io.ramani.ramaniStationary.app.common.presentation.dialogs.BaseBottomSheetDialogFragment
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import kotlinx.android.synthetic.main.fragment_signin_sheet.*
import org.kodein.di.generic.factory

class SigninBottomSheetFragment : BaseBottomSheetDialogFragment() {
    private val DEFAULT_COUNTRY_CODE = 255
    private val viewModelProvider: (Fragment) -> LoginViewModel by factory()
    private lateinit var viewModel: LoginViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel
    private var selectedCountryCode: Int = DEFAULT_COUNTRY_CODE
    private lateinit var flow: AuthFlow

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_signin_sheet, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        flow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)
        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeValidationResponse()
        subscribeLoginResponse()
        viewModel.start()
    }

    private fun subscribeLoginResponse() {
        viewModel.loginActionLiveData.observe(this) {
            // Move to start fragment
            flow.openMainNav()
            dismiss()
        }
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }

    private fun subscribeValidationResponse() {
        viewModel.validationResponseLiveData.observe(this, Observer {
            if (it.first && it.second) {
                phone_et.error = null
                password_pwd_et.error = null
                // TODO: nav to main screen
            } else {
                phone_et.error = if (it.first) null else getString(R.string.required_field)
                password_pwd_et.error = if (it.second) null else getString(R.string.required_field)
            }
        })
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        close_bottom_sheet.setOnClickListener {
            dismiss()
        }
        confirm_button.setOnSingleClickListener {
            viewModel.login(
                selectedCountryCode,
                phone_et.text.toString(),
                password_pwd_et.text.toString()
            )
        }

        setupCountryFlagsAndCodes()
    }

    private fun setupCountryFlagsAndCodes() {
        country_spinner.setCountryForPhoneCode(DEFAULT_COUNTRY_CODE)
        country_spinner.setOnCountryChangeListener {
            selectedCountryCode = country_spinner.selectedCountryCodeAsInt
        }
    }
}