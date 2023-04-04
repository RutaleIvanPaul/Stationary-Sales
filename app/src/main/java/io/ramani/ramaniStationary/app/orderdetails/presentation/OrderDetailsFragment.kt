package io.ramani.ramaniStationary.app.orderdetails.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.history.presentation.HistoryViewModel
import io.ramani.ramaniStationary.data.history.models.response.Item
import kotlinx.android.synthetic.main.fragment_order_details.*
import org.kodein.di.generic.factory

private const val ARG_PARAM_ORDERID = "orderID"

class OrderDetailsFragment : BaseFragment() {
    private var orderID: String? = null
    private val viewModelProvider: (Fragment) -> HistoryViewModel by factory()
    private lateinit var viewModel: HistoryViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var orderDetailsRVAdapter: OrderDetailsRVAdapter
    private val orderProductList = mutableListOf<Item>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        arguments?.let {
            orderID = it.getString(ARG_PARAM_ORDERID)
            viewModel.getOrderDetails(orderID!!)
        }
    }

    override fun initView(view: View?) {
        super.initView(view)

        order_details_rv.layoutManager = LinearLayoutManager(requireContext())
        orderDetailsRVAdapter = OrderDetailsRVAdapter(orderProductList){}
        order_details_rv.adapter = orderDetailsRVAdapter

        initSubscribers()
        initListeners()
    }

    private fun initListeners() {
        print_receipt_orderdetails.setOnSingleClickListener {
            viewModel.getAndPrintReceipt(orderID!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_details, container, false)
    }

    override fun getLayoutResId(): Int = R.layout.fragment_order_details

    private fun initSubscribers() {
        subscribeObservers()
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
    }

    private fun subscribeObservers() {
        viewModel.orderDetailsLiveData.observe(this){
            order_id_value.setText(it.order.id)
            order_date_value.setText(it.order.orderDate)
            order_status_value.setText(it.order.orderStatus)
            print_status_value.setText(it.printStatus)

            total_value.setText(String.format("%s %s",viewModel.currency,it.order.totalCost))

            orderProductList.addAll(it.order.items)
            orderDetailsRVAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        fun newInstance(orderID: String) =
            OrderDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_ORDERID, orderID)
                }
            }
    }
}