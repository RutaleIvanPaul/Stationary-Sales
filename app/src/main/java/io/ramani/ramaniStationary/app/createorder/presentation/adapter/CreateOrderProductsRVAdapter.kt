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
import io.ramani.ramaniStationary.app.createorder.presentation.CreateOrderFragment
import io.ramani.ramaniStationary.app.main.presentation.MAIN_SHARED_MODEL
import io.ramani.ramaniStationary.domain.createorder.model.AvailableProductModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class CreateOrderProductsRVAdapter(
    data: MutableList<ProductModel>,
    private val isRestrictSalesByStockAssigned: Boolean,
    private val availableStockProducts: MutableList<AvailableProductModel>,
    private val currency: String,
    val onItemChanged: (ProductModel) -> Unit
) :
    BaseQuickAdapter<ProductModel, BaseViewHolder>(R.layout.item_product_add, data) {
    override fun convert(holder: BaseViewHolder, item: ProductModel) {
        with(holder) {
            val price = if (item.productCategories.isNotEmpty()) item.productCategories[0].unitPrice else 0

            setText(R.id.item_product_add_name, item.name)
            setText(R.id.item_product_add_price, String.format("%s %s", currency, DecimalFormat("#,###.##").format(price)))

            getView<ImageView>(R.id.item_product_add_imageview).loadImage(item.imagePath, R.mipmap.ic_holder, R.mipmap.ic_holder)

            val onlineMode = MAIN_SHARED_MODEL.isOnline
            val availableStockProducts = availableStockProducts.filter { product -> product.productId == item.id }
            val availableStockAmount = if (availableStockProducts.isNotEmpty()) availableStockProducts.first().quantity else 0

            val quantityTextView = getView<EditText>(R.id.item_product_quantity)
            val quantityWatcher = object: TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    try {
                        val amount = s.trim().toString().toInt()

                        if (onlineMode && isRestrictSalesByStockAssigned) {
                            if (amount <= availableStockAmount) {
                                quantityTextView.setTextColor(Color.BLACK)
                                item.selectedQuantity = amount
                                onItemChanged(item)
                            } else {
                                quantityTextView.setTextColor(Color.RED)
                                item.selectedQuantity = 0
                                onItemChanged(item)
                            }
                        } else {
                            item.selectedQuantity = amount
                            onItemChanged(item)
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            }

            quantityTextView.setText(item.selectedQuantity.toString())
            quantityTextView.apply {
                isEnabled = if (!onlineMode || !isRestrictSalesByStockAssigned) true else availableStockAmount > 0

                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus)
                        addTextChangedListener(quantityWatcher)
                    else
                        removeTextChangedListener(quantityWatcher)
                }
            }

            getView<ImageView>(R.id.item_product_add_plus_button).apply {
                if (onlineMode && isRestrictSalesByStockAssigned) {
                    isEnabled = availableStockAmount > 0

                    setOnClickListener {
                        item.selectedQuantity = ++item.selectedQuantity
                        if (onlineMode && item.selectedQuantity > availableStockAmount)
                            item.selectedQuantity = availableStockAmount

                        quantityTextView.setText(item.selectedQuantity.toString())

                        onItemChanged(item)
                    }
                } else {
                    isEnabled = true

                    setOnClickListener {
                        item.selectedQuantity = ++item.selectedQuantity
                        quantityTextView.setText(item.selectedQuantity.toString())
                        onItemChanged(item)
                    }
                }
            }

            getView<ImageView>(R.id.item_product_add_minus_button).apply {
                if (onlineMode && isRestrictSalesByStockAssigned) {
                    isEnabled = availableStockAmount > 0
                } else {
                    isEnabled = true
                }

                setOnClickListener {
                    item.selectedQuantity = --item.selectedQuantity
                    if (item.selectedQuantity < 0)
                        item.selectedQuantity = 0

                    quantityTextView.setText(item.selectedQuantity.toString())

                    onItemChanged(item)
                }
            }

            getView<TextView>(R.id.item_product_add_available_quantity).apply {
                if (onlineMode) {
                    if (availableStockAmount > 0) {
                        text = String.format("%d %ss Available", availableStockAmount, item.units)
                        setTextColor(ContextCompat.getColor(context, R.color.ramani_green))
                    } else {
                        text = context.resources.getString(R.string.out_of_stock)
                        setTextColor(Color.RED)
                    }
                } else {
                    text = context.resources.getString(R.string.out_of_stock_na)
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

            if (onlineMode && isRestrictSalesByStockAssigned) {
                unitSpinner.isEnabled = availableStockAmount > 0
            } else {
                unitSpinner.isEnabled = true
            }
        }
    }
}