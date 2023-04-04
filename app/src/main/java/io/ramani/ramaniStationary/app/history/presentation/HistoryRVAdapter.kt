package io.ramani.ramaniStationary.app.history.presentation

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.data.history.models.response.Activity

class HistoryRVAdapter(
    data: MutableList<Activity>,
    val onItemClick: (String) -> Unit
) :
    BaseQuickAdapter<Activity, BaseViewHolder>(
        R.layout.sales_history_rv_item,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: Activity) {
        with(helper) {
            setText(R.id.layout_sales_activity_order_id, item.orderId)
            setText(R.id.layout_sales_activity_order_status_value, item.orderStatus)
            setText(R.id.layout_sales_activity_history_shop_name, item.locationName)
            setText(R.id.layout_sales_activity_print_status_label, (String.format("%s: %s", context.getString(R.string.print_status), item.printStatus)))
            getView<TextView>(R.id.layout_sales_activity_view_order_button).setOnSingleClickListener {
                item.orderId?.let { it1 -> onItemClick(it1) }
            }
        }
    }
}