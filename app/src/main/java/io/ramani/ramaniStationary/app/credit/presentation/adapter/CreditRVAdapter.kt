package io.ramani.ramaniStationary.app.credit.presentation.adapter

import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.domain.credit.model.LocationModel
import java.text.NumberFormat
import java.util.*

class CreditRVAdapter(
    data: MutableList<LocationModel>,
    private val currency: String,
    val onItemClick: (String) -> Unit
) :
    BaseQuickAdapter<LocationModel, BaseViewHolder>(R.layout.item_credit, data) {
    override fun convert(helper: BaseViewHolder, item: LocationModel) {
        with(helper) {
            setText(R.id.item_credit_name, item.name)
            setText(R.id.item_credit_max_credit_value, String.format("%s %s", NumberFormat.getNumberInstance(Locale.US).format(item.maxCredit), currency))
            setText(R.id.item_credit_outstanding_credit_value, String.format("%s %s", NumberFormat.getNumberInstance(Locale.US).format(item.creditOrders.outstandingCredit), currency))

            getView<TextView>(R.id.item_credit_view_order_btn).setOnSingleClickListener {
                //item.orderId?.let { it1 -> onItemClick(it1) }
            }

            getView<RecyclerView>(R.id.item_credit_unpaid_orders_list).apply {
                var unpaidOrders = item.creditOrders.unpaidOrderIds
                if (unpaidOrders.isEmpty())
                    unpaidOrders = ArrayList()

                val _adapter = CreditUnpaidOrderRVAdapter(unpaidOrders as MutableList<String>) { unpaidItem ->
                    onItemClick(unpaidItem)
                }

                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = _adapter
            }
        }
    }
}