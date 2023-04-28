package io.ramani.ramaniStationary.app.credit.presentation.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.data.history.models.response.Item
import java.text.NumberFormat
import java.util.*

class CreditOrderItemRVAdapter(
    data: MutableList<Item>,
    private val currency: String
) :
    BaseQuickAdapter<Item, BaseViewHolder>(R.layout.item_order_detail, data) {
    override fun convert(helper: BaseViewHolder, item: Item) {
        with(helper) {
            setText(R.id.item_name_textView, item.productName)
            setText(R.id.item_qty_textView, NumberFormat.getNumberInstance(Locale.US).format(item.quantity))
            setText(R.id.textView_price_category_name, item.vatCategory)
            setText(R.id.textView_price_category_name, String.format("%s %s", NumberFormat.getNumberInstance(Locale.US).format(item.price), currency))
        }
    }
}