package io.ramani.ramaniStationary.app.createorder.presentation.adapter

import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.skydoves.powerspinner.PowerSpinnerView
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.extensions.loadImage
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.domain.home.model.ProductModel

class CreateOrderProdutsRVAdapter(
    data: MutableList<ProductModel>,
    val onItemChanged: (ProductModel, Boolean?, String?) -> Unit
) :
    BaseQuickAdapter<ProductModel, BaseViewHolder>(R.layout.item_product_add, data) {
    override fun convert(holder: BaseViewHolder, item: ProductModel) {
        with(holder) {
            setText(R.id.item_product_add_name, item.name)
            setText(R.id.item_product_add_price, "0")
            setText(R.id.item_product_quantity, item.quantity.toString())

            getView<ImageView>(R.id.item_product_add_imageview).loadImage(item.imagePath, R.mipmap.ic_holder, R.mipmap.ic_holder)
            getView<ImageView>(R.id.item_product_add_plus_button).setOnClickListener {
                onItemChanged(item, true, null)
            }

            getView<ImageView>(R.id.item_product_add_minus_button).setOnClickListener {
                onItemChanged(item, false, null)
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