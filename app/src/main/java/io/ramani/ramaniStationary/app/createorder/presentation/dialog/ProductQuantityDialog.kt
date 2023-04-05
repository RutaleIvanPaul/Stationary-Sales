package io.ramani.ramaniStationary.app.createorder.presentation.dialog

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import kotlinx.android.synthetic.main.dialog_checkout_quantity.*

/**
 * @description     Quantity selection of product dialog
 *
 * @author          Adrian
 */
class ProductQuantityDialog(
    context: Context,
    private val product: ProductModel,
    private val availableStockAmount: Int,
    private val itemPosition: Int,
    val onItemChanged: (Int, ProductModel) -> Unit
) : Dialog(context) {

    private var unit = product.selectedUnit
    private var quantity = product.selectedQuantity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_checkout_quantity)

        setCancelable(false)

        window?.setBackgroundDrawableResource(android.R.color.transparent);
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

        initView()
    }

    private fun initView() {
        dialog_quantity_unit.apply {
            text = unit

            setOnClickListener {
                unit = product.units
                background = ContextCompat.getDrawable(context, R.drawable.rounded_rectangle_small_radius)
                dialog_quantity_unit_secondary.background = null
            }
        }

        val secondaryUnit = findViewById<TextView>(R.id.dialog_quantity_unit_secondary)
        if (product.hasSecondaryUnitConversion && !TextUtils.isEmpty(product.secondaryUnitName)) {
            secondaryUnit.visibility = View.VISIBLE
            secondaryUnit.text = product.secondaryUnitName
        } else {
            secondaryUnit.visibility = View.GONE
        }
        secondaryUnit.setOnClickListener {
            unit = product.secondaryUnitName

            secondaryUnit.background = ContextCompat.getDrawable(context, R.drawable.rounded_rectangle_small_radius)
            dialog_quantity_unit.background = null
        }

        dialog_quantity_field.apply {
            setText(product.selectedQuantity.toString())

            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    var isOkayClickable = false

                    if (s.toString().isNotEmpty()) {
                        quantity = s.toString().toInt()

                        if (s.toString().toInt() > availableStockAmount) {
                            dialog_quantity_warning.visibility = View.VISIBLE
                        } else {
                            dialog_quantity_warning.visibility = View.INVISIBLE
                            isOkayClickable = true
                        }
                    } else {
                        quantity = 0
                    }

                    dialog_quantity_okay.isEnabled = isOkayClickable
                }
            })
        }

        dialog_quantity_minus.setOnClickListener {
            quantity -= 1

            if (quantity < 0) {
                quantity = 0
            }

            dialog_quantity_okay.isEnabled = quantity > 0
            dialog_quantity_field.setText(quantity.toString())
        }

        dialog_quantity_plus.setOnClickListener {
            quantity += 1

            var isOkayClickable = false

            if (quantity > availableStockAmount) {
                dialog_quantity_warning.visibility = View.VISIBLE
            } else {
                dialog_quantity_warning.visibility = View.INVISIBLE
                isOkayClickable = true
            }

            dialog_quantity_field.setText(quantity.toString())
            dialog_quantity_okay.isEnabled = isOkayClickable
        }

        dialog_quantity_okay.setOnClickListener {
            val _quantity = dialog_quantity_field.text.trim().toString()
            if (_quantity.isNotEmpty()) {
                product.selectedUnit = unit
                product.selectedQuantity = _quantity.toInt()
                dismiss()

                onItemChanged(itemPosition, product)
            }
        }
    }

}