package io.ramani.ramaniStationary.app.createmerchant.presentation.dialog

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.skydoves.powerspinner.PowerSpinnerView
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.createmerchant.presentation.CreateMerchantViewModel
import io.ramani.ramaniStationary.app.createorder.presentation.CreateOrderViewModel
import io.ramani.ramaniStationary.data.createmerchant.models.request.MerchantRouteModel
import io.ramani.ramaniStationary.data.createmerchant.models.request.MetaDataItem
import io.ramani.ramaniStationary.data.createmerchant.models.request.RegisterMerchantRequestModel
import io.ramani.ramaniStationary.domain.home.model.MerchantMemberModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import kotlinx.android.synthetic.main.dialog_create_new_merchant.*

/**
 * @description     Create new merchant dialog
 *
 * @author          Adrian
 */
class CreateNewMerchantDialog(
    context: Context,
    val viewModel: BaseViewModel,
    val fragment: BaseFragment, /* Parent Fragment */
    val onItemAdded: (MerchantModel) -> Unit
) : Dialog(context) {

    private val DEFAULT_COUNTRY_CODE = 255

    // Constant merchant type
    private val merchantTypes = mutableListOf(2 /* Retail */, 3 /* WHOLESALES */, 6 /* CONSUMER */)
    var selectedMerchantType = -1
    private var selectedCountryCode: Int = DEFAULT_COUNTRY_CODE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_create_new_merchant)

        setCancelable(false)

        window?.setBackgroundDrawableResource(android.R.color.transparent);
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

        if (viewModel is CreateMerchantViewModel) {
            viewModel.onMerchantAddedLiveData.observe(fragment) {
                processMerchant(it)
            }
        } else if (viewModel is CreateOrderViewModel) {
            viewModel.onMerchantAddedLiveData.observe(fragment) {
                processMerchant(it)
            }
        }

        initView()
    }

    private fun initView() {
        val typeSpinner = findViewById<PowerSpinnerView>(R.id.dialog_create_merchant_type)

        typeSpinner.setItems(R.array.merchant_types)
        typeSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
            selectedMerchantType = merchantTypes[newIndex]
        }

        dialog_create_merchant_country_spinner.setCountryForPhoneCode(DEFAULT_COUNTRY_CODE)
        dialog_create_merchant_country_spinner.setOnCountryChangeListener {
            selectedCountryCode = dialog_create_merchant_country_spinner.selectedCountryCodeAsInt
        }

        dialog_create_merchant_add.setOnClickListener {
            // validate
            val name = dialog_create_merchant_name_textfield.text.trim().toString()
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(context, R.string.hint_customer_name, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var number = dialog_create_merchant_phone_textfield.text.trim().toString()
            if (TextUtils.isEmpty(number)) {
                Toast.makeText(context, R.string.hint_mobile_number, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (number.length != 10 && number.length != 9) {
                Toast.makeText(context, R.string.invalid_phone_number, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                number = selectedCountryCode.toString() + number
            }

            if (selectedMerchantType == -1) {
                Toast.makeText(context, R.string.hint_customer_type, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val shopName = dialog_create_merchant_shopname_textfield.text.trim().toString()
            if (TextUtils.isEmpty(shopName)) {
                Toast.makeText(context, R.string.hint_shop_name, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val street = dialog_create_merchant_location_textfield.text.trim().toString()
            val tin = dialog_create_merchant_tin_textfield.text.trim().toString()
            val vrn = dialog_create_merchant_vrn_textfield.text.trim().toString()

            if (viewModel is CreateMerchantViewModel) {
                val sellerId = viewModel.companyId
                val salesPersonUID = viewModel.userId
                val salesPersonName = viewModel.userModel.name

                dialog_create_merchant_loader.visible(true)

                // Submit
                val merchantRequest = RegisterMerchantRequestModel(
                    name, "0, 0", sellerId, salesPersonUID, salesPersonName,
                    merchantStatus = 0,
                    categories = 0,
                    merchantType = selectedMerchantType,
                    merchantTIN = tin,
                    merchantVRN = vrn,
                    members = mutableListOf(MerchantMemberModel(name, phoneNumber = number)),
                    isActive = true,
                    routes = mutableListOf(MerchantRouteModel("WalkIn", "WalkIn")),
                    metaData = mutableListOf(MetaDataItem("WalkIn", "WalkIn", "WalkIn"))
                )

                it.isEnabled = false

                viewModel.registerMerchant(merchantRequest)
            } else if (viewModel is CreateOrderViewModel) {
                val sellerId = viewModel.companyId
                val salesPersonUID = viewModel.userId
                val salesPersonName = viewModel.userModel.name

                dialog_create_merchant_loader.visible(true)

                // Submit
                val merchantRequest = RegisterMerchantRequestModel(
                    name, "0, 0", sellerId, salesPersonUID, salesPersonName,
                    merchantStatus = 0,
                    categories = 0,
                    merchantType = selectedMerchantType,
                    merchantTIN = tin,
                    merchantVRN = vrn,
                    members = mutableListOf(MerchantMemberModel(name, phoneNumber = number)),
                    isActive = true,
                    routes = mutableListOf(MerchantRouteModel("WalkIn", "WalkIn")),
                    metaData = mutableListOf(MetaDataItem("WalkIn", "WalkIn", "WalkIn"))
                )

                it.isEnabled = false

                viewModel.registerMerchant(merchantRequest)
            }
        }
    }

    override fun onBackPressed() {
        openWarningDialog()
    }

    private fun processMerchant(response: Pair<MerchantModel?, String>) {
        if (response.first != null) {
            dialog_create_merchant_loader.visible(false)

            onItemAdded(response.first!!)
            dismiss()
        } else {
            // Error
            Toast.makeText(context, response.second, Toast.LENGTH_SHORT).show()
            dialog_create_merchant_add.isEnabled = true
        }
    }

    private fun openWarningDialog() {
        val confirmDialog = AlertDialog.Builder(context)
        confirmDialog.apply {
            setTitle(R.string.warning_cancel_creating_merchant)

            setPositiveButton(R.string.okay) { dialog, which ->
                dismiss()
            }

            setNegativeButton(android.R.string.cancel, null)

            show()
        }
    }

}