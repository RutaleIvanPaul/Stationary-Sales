package io.ramani.ramaniStationary.app.createorder.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.createorder.presentation.adapter.CheckoutProductsRVAdapter
import io.ramani.ramaniStationary.app.createorder.presentation.adapter.ItemSelectionType
import io.ramani.ramaniStationary.app.home.flow.HomeFlow
import io.ramani.ramaniStationary.app.home.flow.HomeFlowController
import kotlinx.android.synthetic.main.fragment_checkout.*
import org.kodein.di.generic.factory
import java.util.*

class CheckoutFragment : BaseFragment() {
    companion object {
        fun newInstance() = CheckoutFragment()
    }

    private val viewModelProvider: (Fragment) -> CreateOrderViewModel by factory()
    private lateinit var viewModel: CreateOrderViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: HomeFlow

    override fun getLayoutResId(): Int = R.layout.fragment_checkout

    private lateinit var productsAdapter: CheckoutProductsRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
    }

    override fun initView(view: View?) {
        super.initView(view)

        flow = HomeFlowController(baseActivity!!, R.id.main_fragment_container)

        checkout_order_date.text = viewModel.dateFormatter.getCalendarFullTime(Date())

        checkout_add_product.setOnSingleClickListener {
            requireActivity().onBackPressed()
        }

        initSubscribers()
        updateRV()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeResponse()
        viewModel.getMerchants()
    }

    private fun subscribeResponse() {
        viewModel.onMerchantsLoadedLiveData.observe(this) {
            checkout_select_customer_spinner.apply {
                setItems(viewModel.merchantNameList)
                setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->

                }
            }
        }
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        //loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun updateRV() {
        val products = CREATE_ORDER_MODEL.productsToBeOrdered

        productsAdapter = CheckoutProductsRVAdapter(products , viewModel.availableStockProductList) { item, type ->
            when (type) {
                ItemSelectionType.QTY -> {}
                else -> {}
            }
        }

        checkout_product_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = productsAdapter
        }
    }

}