package io.ramani.ramaniStationary.app.createorder.presentation.adapter

import android.annotation.SuppressLint
import android.widget.RadioButton
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.domain.home.model.ProductCategoryModel
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class CheckoutPriceCategoriesRVAdapter(
    data: MutableList<ProductCategoryModel>,
    private val selectedCategory: ProductCategoryModel?,
    private val currency: String,
    val onItemSelected: (ProductCategoryModel) -> Unit
) :
    BaseQuickAdapter<ProductCategoryModel, BaseViewHolder>(R.layout.item_checkout_price_category_item, data) {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: ProductCategoryModel) {
        with(holder) {
            getView<RadioButton>(R.id.item_checkout_price_category_name).apply {
                text = item.name.replaceFirstChar { it.uppercase() }
                isChecked = selectedCategory?.name == item.name
            }

            getView<TextView>(R.id.item_checkout_price_category_price).text = "${currency.uppercase()} ${DecimalFormat("#,###.##").format(item.unitPrice)}"

            itemView.setOnClickListener {
                onItemSelected(item)
            }
        }
    }
}
