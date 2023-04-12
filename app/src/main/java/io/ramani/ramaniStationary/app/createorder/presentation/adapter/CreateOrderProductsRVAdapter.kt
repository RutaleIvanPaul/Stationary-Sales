package io.ramani.ramaniStationary.app.createorder.presentation.adapter

import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
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
    val onItemChanged: (ProductModel) -> Unit
) :
    BaseQuickAdapter<ProductModel, BaseViewHolder>(R.layout.item_product_add, data) {
    override fun convert(holder: BaseViewHolder, item: ProductModel) {
        with(holder) {
            val price = if (item.productCategories.isNotEmpty()) item.productCategories[0].unitPrice.toInt() else 0

            setText(R.id.item_product_add_name, item.name)
            setText(R.id.item_product_add_price, String.format("Tsh %s", NumberFormat.getNumberInstance(Locale.US).format(price)))
            setText(R.id.item_product_quantity, item.selectedQuantity.toString())

            getView<ImageView>(R.id.item_product_add_imageview).loadImage(item.imagePath, R.mipmap.ic_holder, R.mipmap.ic_holder)

            val availableStockProducts = availableStockProducts.filter { product -> product.productId == item.id }
            val availableStockAmount = if (availableStockProducts.isNotEmpty()) availableStockProducts.first().quantity else 0

            val quantityTextView = getView<EditText>(R.id.item_product_quantity)
            quantityTextView.apply {
                isEnabled = availableStockAmount > 0

                addTextChangedListener(object: TextWatcher {
                    override fun afterTextChanged(s: Editable) {}
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        val isOkay = false

                        try {
                            val amount = s.trim().toString().toInt()
                            if (amount <= availableStockAmount) {
                                getView<EditText>(R.id.item_product_quantity).setTextColor(Color.BLACK)
                                item.selectedQuantity = amount
                                onItemChanged(item)
                            } else {
                                getView<EditText>(R.id.item_product_quantity).setTextColor(Color.RED)
                                item.selectedQuantity = 0
                                onItemChanged(item)
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    }
                })
            }

            getView<ImageView>(R.id.item_product_add_plus_button).apply {
                this.isEnabled = availableStockAmount > 0

                setOnClickListener {
                    item.selectedQuantity = ++item.selectedQuantity
                    if (item.selectedQuantity > availableStockAmount)
                        item.selectedQuantity = availableStockAmount

                    quantityTextView.setText(item.selectedQuantity.toString())

                    onItemChanged(item)
                }
            }

            getView<ImageView>(R.id.item_product_add_minus_button).apply {
                this.isEnabled = availableStockAmount > 0

                setOnClickListener {
                    item.selectedQuantity = --item.selectedQuantity
                    if (item.selectedQuantity < 0)
                        item.selectedQuantity = 0

                    quantityTextView.setText(item.selectedQuantity.toString())

                    onItemChanged(item)
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
                item.selectedUnit = newItem
                onItemChanged(item)
            }

            unitSpinner.dismiss()
            unitSpinner.isEnabled = availableStockAmount > 0
        }
    }
}