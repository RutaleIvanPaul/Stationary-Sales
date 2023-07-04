package io.ramani.ramaniStationary.app.stock.presentation

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.data.stock.models.response.ProductsItem

class AvailableStockRVAdapter(
    data: MutableList<ProductsItem>,
    val onItemClick: (ProductsItem) -> Unit
) :
    BaseQuickAdapter<ProductsItem, BaseViewHolder>(
        R.layout.available_stock_rv_item,
        data
    ) {
    override fun convert(helper: BaseViewHolder, item: ProductsItem) {
        with(helper) {
            setText(R.id.available_stock_product_name, item.productName)
            setText(R.id.available_stock_product_quantity, item.quantity!!.toString())
        }
    }
}