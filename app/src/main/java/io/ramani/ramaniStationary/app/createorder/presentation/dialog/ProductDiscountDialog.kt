package io.ramani.ramaniStationary.app.createorder.presentation.dialog

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import kotlinx.android.synthetic.main.dialog_checkout_discount.*
import java.text.NumberFormat
import java.util.*

/**
 * @description     Discount Dialog
 *
 * @author          Adrian
 */
class ProductDiscountDialog(
    context: Context,
    private val product: ProductModel,
    private val itemPosition: Int,
    val onItemChanged: (Int, ProductModel) -> Unit
) : Dialog(context) {

    private var unit = product.selectedUnit
    private var quantity = product.selectedQuantity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_checkout_discount)

        setCancelable(false)

        window?.setBackgroundDrawableResource(android.R.color.transparent);
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

        initView()
    }

    private fun initView() {
        dialog_discount_custom_button.apply {
            setOnClickListener {
                background = ContextCompat.getDrawable(context, R.drawable.rounded_rectangle_small_radius)
                dialog_discount_predefined_button.background = null

                dialog_discount_custom_layout.visibility = View.VISIBLE
                dialog_discount_predefined_layout.visibility = View.INVISIBLE
            }
        }

        dialog_discount_predefined_button.apply {
            setOnClickListener {
                background = ContextCompat.getDrawable(context, R.drawable.rounded_rectangle_small_radius)
                dialog_discount_custom_button.background = null

                dialog_discount_custom_layout.visibility = View.INVISIBLE
                dialog_discount_predefined_layout.visibility = View.VISIBLE
            }
        }

        dialog_discount_original_price.text = NumberFormat.getNumberInstance(Locale.US).format(product.selectedPriceCategory?.unitPrice)

        dialog_discount_okay.setOnClickListener {
            dismiss()

            onItemChanged(itemPosition, product)
        }
    }

    override fun onBackPressed() {
        dismiss()
    }
}