package io.ramani.ramaniStationary.app.credit.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.credit.presentation.adapter.CreditOrderItemRVAdapter
import io.ramani.ramaniStationary.app.home.flow.HomeFlow
import io.ramani.ramaniStationary.app.home.flow.HomeFlowController
import io.ramani.ramaniStationary.data.history.models.response.Item
import io.ramani.ramaniStationary.data.history.models.response.OrderDetailsResponse
import kotlinx.android.synthetic.main.fragment_credit.*
import kotlinx.android.synthetic.main.fragment_order_detail_credit.*
import kotlinx.android.synthetic.main.view_order_status_summary.*
import org.kodein.di.generic.factory

private const val ARG_PARAM_ORDER_ID = "orderID"

class CreditOrderDetailFragment : BaseFragment() {
    companion object {
        fun newInstance(orderID: String) = CreditOrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_ORDER_ID, orderID)
                }
            }
    }

    private val viewModelProvider: (Fragment) -> CreditViewModel by factory()
    private lateinit var viewModel: CreditViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: HomeFlow

    override fun getLayoutResId(): Int = R.layout.fragment_order_detail_credit

    private lateinit var adapter: CreditOrderItemRVAdapter
    private var orderID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
    }

    override fun initView(view: View?) {
        super.initView(view)

        flow = HomeFlowController(baseActivity!!, R.id.main_fragment_container)

        initSubscribers()

        arguments?.let {
            orderID = it.getString(ARG_PARAM_ORDER_ID) ?: ""
            viewModel.getOrderDetails(orderID)
        }

        fragment_order_detail_credit_detail_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        order_details_title.setOnClickListener {
            requireActivity().onBackPressed()
        }

        fragment_order_detail_credit_order_menu.setOnSingleClickListener {
            viewModel.updateOrderPaymentStatusToPaid(orderID)
        }
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
        viewModel.onOrderDetailLoadedLiveData.observe(this) {
            updateUI(it)
            updateRV(it)
        }

        viewModel.onUpdatedPaymentStatusLiveData.observe(this) {
            if (it) {
                order_id_textView_payment_status.text = getString(R.string.paid)
            }
        }
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        fragment_order_detail_credit_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun updateRV(details: OrderDetailsResponse) {
        var items = details.order.items
        if (items.isEmpty())
            items = mutableListOf()

        adapter = CreditOrderItemRVAdapter(items as MutableList<Item>, viewModel.currency)
        order_items_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = adapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(details: OrderDetailsResponse) {
        details.order.apply {
            order_status__textView.text = orderStatus
            order_id_textView.text = orderID
            order_id_textView_payment_status.text = paymentStatus
            order_print_status_label.text = "${getString(R.string.print_status)} ${details.printStatus}"
        }

        order_supplier_name_textView.text = details.sellerName
        order_date__textview.text = details.order.orderDate
        order_total__textView.text = viewModel.getFormattedAmount(details.order.totalCost, true)

        delivery_status_textView.text = details.order.deliveryStatus
        deliver_branch_textView.text = details.buyerName
        delivery_eta__textView.text = details.order.deliveryDate

        if (details.order.comment.isNotEmpty())
            order_comment_textView.text = details.order.comment
    }
}