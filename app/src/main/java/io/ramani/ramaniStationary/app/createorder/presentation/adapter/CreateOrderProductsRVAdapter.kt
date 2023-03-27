package io.ramani.ramaniStationary.app.createorder.presentation.adapter

import android.graphics.Color
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.skydoves.powerspinner.PowerSpinnerView
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.extensions.loadImage
import io.ramani.ramaniStationary.domain.createorder.model.AvailableProductModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import java.text.NumberFormat
import java.util.*

class CreateOrderProductsRVAdapter(
    data: MutableList<ProductModel>,
    private val availableStockProducts: MutableList<AvailableProductModel>,
    val onItemChanged: (ProductModel, Boolean?, String?) -> Unit
) :
    BaseQuickAdapter<ProductModel, BaseViewHolder>(R.layout.item_product_add, data) {
    override fun convert(holder: BaseViewHolder, item: ProductModel) {
        with(holder) {
            val price = if (item.productCategories.isNotEmpty()) item.productCategories[0].unitPrice.toInt() else 0

            setText(R.id.item_product_add_name, item.name)
            setText(R.id.item_product_add_price, String.format("Tsh %s", NumberFormat.getNumberInstance(Locale.US).format(price)))
            setText(R.id.item_product_quantity, item.quantity.toString())

            getView<ImageView>(R.id.item_product_add_imageview).loadImage(item.imagePath, R.mipmap.ic_holder, R.mipmap.ic_holder)

            val availableStockProducts = availableStockProducts.filter { product -> product.productId == item.id }
            val availableStockAmount = if (availableStockProducts.isNotEmpty()) availableStockProducts.first().quantity else 0
            getView<ImageView>(R.id.item_product_add_plus_button).apply {
                this.isEnabled = availableStockAmount > 0

                setOnClickListener {
                    onItemChanged(item, true, null)
                }
            }

            getView<ImageView>(R.id.item_product_add_minus_button).apply {
                this.isEnabled = availableStockAmount > 0

                setOnClickListener {
                    onItemChanged(item, false, null)
                }
            }

            getView<TextView>(R.id.item_product_add_available_quantity).apply {
                if (availableStockAmount > 0) {
                    text = String.format("%d %ss Available", availableStockAmount, item.units)
                    setTextColor(ContextCompat.getColor(context, R.color.ramani_green))
                } else {
                    text = context.resources.getString(R.string.out_of_stock)
                    setTextColor(Color.RED)
                }
            }

            val unitSpinner = getView<PowerSpinnerView>(R.id.item_product_add_select_unit)
            unitSpinner.text = if (!TextUtils.isEmpty(item.selectedUnit)) item.selectedUnit else item.units

            val allUnits = mutableListOf<String>()
            if (!TextUtils.isEmpty(item.units))
                allUnits.add(item.units)
            if (!TextUtils.isEmpty(item.secondaryUnitName))
                allUnits.add(item.secondaryUnitName)

            unitSpinner.setItems(allUnits)

            unitSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                onItemChanged(item, null, newItem)
            }

            unitSpinner.dismiss()
        }
    }
}