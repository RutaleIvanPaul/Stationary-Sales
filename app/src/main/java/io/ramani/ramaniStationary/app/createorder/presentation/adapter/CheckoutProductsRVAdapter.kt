package io.ramani.ramaniStationary.app.createorder.presentation.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import java.text.NumberFormat
import java.util.*

class CheckoutProductsRVAdapter(
    data: MutableList<ProductModel>,
    val onItemSelected: (Int, ProductModel, ItemSelectionType?) -> Unit
) :
    BaseQuickAdapter<ProductModel, BaseViewHolder>(R.layout.item_checkout_product, data) {
    override fun convert(holder: BaseViewHolder, item: ProductModel) {
        with(holder) {
            val price = item.selectedPriceCategory?.unitPrice ?: 0

            setText(R.id.item_checkout_product_name, item.name)
            setText(R.id.item_checkout_product_price, String.format("%s Tsh", NumberFormat.getNumberInstance(Locale.US).format(price)))
            setText(R.id.item_checkout_product_quantity, String.format("%d %s", item.selectedQuantity, item.selectedUnit))

            getView<View>(R.id.item_checkout_product_quantity_row).setOnSingleClickListener {
                onItemSelected(getItemPosition(item), item, ItemSelectionType.QTY)
            }

            getView<View>(R.id.item_checkout_product_price_category_row).setOnSingleClickListener {
                onItemSelected(getItemPosition(item), item, ItemSelectionType.PRICE_CATEGORY)
            }
            setText(R.id.item_checkout_product_price_category, String.format("%s Price", item.selectedPriceCategory?.name?.replaceFirstChar { it.uppercase() }))

            getView<View>(R.id.item_checkout_product_discount_row).setOnSingleClickListener {
                onItemSelected(getItemPosition(item), item, ItemSelectionType.DISCOUNT)
            }

            getView<View>(R.id.item_checkout_product_rewards_row).setOnSingleClickListener {
                onItemSelected(getItemPosition(item), item, ItemSelectionType.REWARDS)
            }

            getView<View>(R.id.item_checkout_product_delete_button).setOnSingleClickListener {
                onItemSelected(getItemPosition(item), item, ItemSelectionType.DELETE)
            }

        }
    }
}

enum class ItemSelectionType {
    QTY, PRICE_CATEGORY, DISCOUNT, REWARDS, DELETE
}
