package io.ramani.ramaniStationary.app.createorder.presentation

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
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import kotlinx.android.synthetic.main.fragment_create_order.*
import org.kodein.di.generic.factory

class CreateOrderFragment : BaseFragment() {
    companion object {
        fun newInstance() = CreateOrderFragment()
    }

    private val viewModelProvider: (Fragment) -> CreateOrderViewModel by factory()
    private lateinit var viewModel: CreateOrderViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: CreateOrderFlow
    private lateinit var authFlow: AuthFlow

    override fun getLayoutResId(): Int = R.layout.fragment_create_order

    private lateinit var productsAdapter: CreateOrderProductsRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
    }

    override fun initView(view: View?) {
        super.initView(view)

        flow = CreateOrderFlowController(baseActivity!!, R.id.main_fragment_container)
        authFlow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)

        create_order_search_textfield.addTextChangedListener(searchTextWatcher)
        create_order_search_textfield.setOnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_SEARCH -> {
                    hideKeyboard()
                    true
                }
                else -> false
            }
        }

        create_order_checkout.setOnSingleClickListener {
            flow.openCheckout()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            initSubscribers()
            updateCheckOutStatus()
        }, 300)
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
        viewModel.onProductsLoadedLiveData.observe(this) {

        }

        viewModel.onAvailableStockProductsLoadedLiveData.observe(this) {
            updateRV()
        }

        CREATE_ORDER_MODEL.onOrderedProductsUpdatedLiveData.observe(this) {
            updateRV()
            updateCheckOutStatus()
        }
    }

    override fun onDestroy() {
        if (isAdded)
            create_order_search_textfield.removeTextChangedListener(searchTextWatcher)

        super.onDestroy()
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)

        if (MAIN_SHARED_MODEL.isOnline)
            errorDialog(error)
    }

    private fun updateRV() {
        val keyword = create_order_search_textfield.text.trim().toString()
        val products = if (keyword.isNotEmpty()) viewModel.productList.filter { product -> product.name.contains(keyword, true) } else viewModel.productList

        productsAdapter = CreateOrderProductsRVAdapter(products as MutableList<ProductModel>, viewModel.isRestrictSalesByStockAssigned, viewModel.availableStockProductList) { item ->
            // Add or remove product from orders
            CREATE_ORDER_MODEL.addOrRemoveProduct(item)

            //productsAdapter.notifyDataSetChanged()
            updateCheckOutStatus()
        }

        create_order_product_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = productsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun updateCheckOutStatus() {
        create_order_checkout.isEnabled = CREATE_ORDER_MODEL.productsToBeOrdered.isNotEmpty()
        create_order_total_price.text = String.format("TSH %s", viewModel.getFormattedAmount(CREATE_ORDER_MODEL.getTotalOrderedPrice()))
    }

    private val searchTextWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            updateRV()
        }
    }
}