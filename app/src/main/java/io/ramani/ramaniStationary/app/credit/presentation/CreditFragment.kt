package io.ramani.ramaniStationary.app.credit.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.auth.flow.AuthFlow
import io.ramani.ramaniStationary.app.auth.flow.AuthFlowController
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.hideKeyboard
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.createorder.flow.CreateOrderFlow
import io.ramani.ramaniStationary.app.createorder.flow.CreateOrderFlowController
import io.ramani.ramaniStationary.app.createorder.presentation.adapter.CreateOrderProductsRVAdapter
import io.ramani.ramaniStationary.app.main.presentation.MAIN_SHARED_MODEL
import io.ramani.ramaniStationary.domain.credit.model.LocationModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import kotlinx.android.synthetic.main.fragment_create_order.*
import kotlinx.android.synthetic.main.fragment_credit.*
import org.kodein.di.generic.factory

class CreditFragment : BaseFragment() {
    companion object {
        fun newInstance() = CreditFragment()
    }

    private val viewModelProvider: (Fragment) -> CreditViewModel by factory()
    private lateinit var viewModel: CreditViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: CreateOrderFlow
    private lateinit var authFlow: AuthFlow

    override fun getLayoutResId(): Int = R.layout.fragment_credit

    private lateinit var creditAdapter: CreditRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
    }

    override fun initView(view: View?) {
        super.initView(view)

        flow = CreateOrderFlowController(baseActivity!!, R.id.main_fragment_container)
        authFlow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)

        credit_search_textfield.addTextChangedListener(searchTextWatcher)
        credit_search_textfield.setOnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_SEARCH -> {
                    hideKeyboard()
                    true
                }
                else -> false
            }
        }

        credit_refresh_btn.setOnSingleClickListener {
            if (!viewModel.isLoadingLocations)
                viewModel.getCredits(true)
        }

        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeResponse()
        viewModel.start()
    }

    private fun subscribeResponse() {
        viewModel.onLocationLoadedLiveData.observe(this) {
            updateUI()
            updateRV()
        }
    }

    override fun onDestroy() {
        if (isAdded && credit_search_textfield != null)
            credit_search_textfield.removeTextChangedListener(searchTextWatcher)

        super.onDestroy()
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        credit_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun updateRV() {
        val keyword = credit_search_textfield.text.trim().toString()
        val filteredList = if (keyword.isNotEmpty()) viewModel.locationList.filter { location -> location.name.contains(keyword, true) } else viewModel.locationList

        creditAdapter = CreditRVAdapter(filteredList as MutableList<LocationModel>, viewModel.currency) { item ->

        }

        credit_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = creditAdapter
        }
    }

    private fun updateUI() {
        credit_total_outstanding.text = viewModel.getFormattedAmount(viewModel.totalOutstanding, true)
        credit_total_number_of_orders.text = viewModel.getFormattedAmount(viewModel.totalOrders)
        credit_total_number_of_merchants.text = viewModel.getFormattedAmount(viewModel.totalMerchants)
    }

    private val searchTextWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            updateRV()
        }
    }
}