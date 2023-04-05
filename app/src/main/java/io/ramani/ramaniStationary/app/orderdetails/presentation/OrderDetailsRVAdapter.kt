package io.ramani.ramaniStationary.app.orderdetails.presentation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.data.history.models.response.Activity
import io.ramani.ramaniStationary.data.history.models.response.Item

class OrderDetailsRVAdapter(
    data: MutableList<Item>,
    private val prefs: PrefsManager,
    val onItemClick: (String) -> Unit
) :
    BaseQuickAdapter<Item, BaseViewHolder>(
        R.layout.item_order_details,
        data
    ){
    override fun convert(holder: BaseViewHolder, item: Item) {
        with(holder){
            setText(R.id.product_name,item.productName)
            setText(R.id.product_cost,String.format("%s %s",prefs.currency,item.price.toString()))
        }
    }

}