package io.ramani.ramaniStationary.app.createmerchant.presentation.dialog

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.widget.Toast
import com.skydoves.powerspinner.PowerSpinnerView
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.createmerchant.presentation.CreateMerchantViewModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import kotlinx.android.synthetic.main.dialog_create_new_merchant.*

/**
 * @description     Create new merchant dialog
 *
 * @author          Adrian
 */
class CreateNewMerchantDialog(
    context: Context,
    val viewModel: CreateMerchantViewModel,
    val onItemAdded: (MerchantModel) -> Unit
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_create_new_merchant)

        setCancelable(false)

        window?.setBackgroundDrawableResource(android.R.color.transparent);
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

        initView()
    }

    private fun initView() {
        val typeSpinner = findViewById<PowerSpinnerView>(R.id.dialog_create_merchant_type)

        typeSpinner.setItems(R.array.merchant_types)
        typeSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->

        }

        dialog_create_merchant_add.setOnClickListener {
            // validate
            val name = dialog_create_merchant_name_textfield.text.trim()
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(context, R.string.hint_customer_name, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val number = dialog_create_merchant_phone_textfield.text.trim()
            if (TextUtils.isEmpty(number)) {
                Toast.makeText(context, R.string.hint_mobile_number, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val type = dialog_create_merchant_type.text.trim()
            if (TextUtils.isEmpty(type)) {
                Toast.makeText(context, R.string.hint_customer_type, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val shopName = dialog_create_merchant_shopname_textfield.text.trim()
            if (TextUtils.isEmpty(shopName)) {
                Toast.makeText(context, R.string.hint_shop_name, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val street = dialog_create_merchant_location_textfield.text.trim()

            // Submit

            dismiss()
        }
    }

}