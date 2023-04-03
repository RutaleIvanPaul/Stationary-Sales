package io.ramani.ramaniStationary.app.createorder.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.auth.flow.AuthFlow
import io.ramani.ramaniStationary.app.auth.flow.AuthFlowController
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.createorder.flow.CreateOrderFlow
import io.ramani.ramaniStationary.app.createorder.flow.CreateOrderFlowController
import kotlinx.android.synthetic.main.fragment_order_complete.*
import org.kodein.di.generic.factory

class OrderCompletedFragment : BaseFragment() {
    companion object {
        fun newInstance() = OrderCompletedFragment()
    }

    private val viewModelProvider: (Fragment) -> CreateOrderViewModel by factory()
    private lateinit var viewModel: CreateOrderViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: CreateOrderFlow
    private lateinit var authFlow: AuthFlow

    override fun getLayoutResId(): Int = R.layout.fragment_order_complete

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
    }

    override fun initView(view: View?) {
        super.initView(view)

        flow = CreateOrderFlowController(baseActivity!!, R.id.main_fragment_container)
        authFlow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)

        fragment_order_complete_new_sale.setOnSingleClickListener {
            //flow.openAddItems()
            authFlow.openMainNav()
        }

        fragment_order_complete_print_receipt.setOnSingleClickListener {
            flow.openPrintSuccessful()
        }

        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeResponse()
        //viewModel.start()
    }

    private fun subscribeResponse() {

    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        //loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }
}