package io.ramani.ramaniStationary.app.credit.presentation.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniStationary.R

class CreditUnpaidOrderRVAdapter(
    data: MutableList<String>,
    val onItemClick: (String) -> Unit
) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_credit_unpaid_order, data) {
    override fun convert(helper: BaseViewHolder, item: String) {
        with(helper) {
            getView<TextView>(R.id.item_credit_unpaid_order).apply {
                text = item

                setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }
}