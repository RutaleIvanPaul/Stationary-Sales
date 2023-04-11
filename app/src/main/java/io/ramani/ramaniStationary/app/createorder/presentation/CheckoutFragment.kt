package io.ramani.ramaniStationary.app.createorder.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.auth.flow.AuthFlow
import io.ramani.ramaniStationary.app.auth.flow.AuthFlowController
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.extensions.visible
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.createmerchant.presentation.dialog.CreateNewMerchantDialog
import io.ramani.ramaniStationary.app.createorder.flow.CreateOrderFlow
import io.ramani.ramaniStationary.app.createorder.flow.CreateOrderFlowController
import io.ramani.ramaniStationary.app.createorder.presentation.adapter.CheckoutProductsRVAdapter
import io.ramani.ramaniStationary.app.createorder.presentation.adapter.ItemSelectionType
import io.ramani.ramaniStationary.app.createorder.presentation.dialog.ProductDiscountDialog
import io.ramani.ramaniStationary.app.createorder.presentation.dialog.ProductPriceCategoryDialog
import io.ramani.ramaniStationary.app.createorder.presentation.dialog.ProductQuantityDialog
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import kotlinx.android.synthetic.main.fragment_checkout.*
import org.kodein.di.generic.factory
import java.text.NumberFormat
import java.util.*

class CheckoutFragment : BaseFragment() {
    companion object {
        fun newInstance() = CheckoutFragment()
    }

    private val viewModelProvider: (Fragment) -> CreateOrderViewModel by factory()
    private lateinit var viewModel: CreateOrderViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: CreateOrderFlow
    private lateinit var authFlow: AuthFlow

    override fun getLayoutResId(): Int = R.layout.fragment_checkout

    private lateinit var productsAdapter: CheckoutProductsRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
    }

    override fun initView(view: View?) {
        super.initView(view)

        flow = CreateOrderFlowController(baseActivity!!, R.id.main_fragment_container)
        authFlow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)

        checkout_order_date.text = viewModel.dateFormatter.getCalendarFullTime(Date())

        checkout_add_product.setOnSingleClickListener {
            requireActivity().onBackPressed()
        }

        // Add new customer
        checkout_customer_add_new.setOnSingleClickListener {
            val dialog = CreateNewMerchantDialog(requireActivity(), viewModel, this) { merchant ->
                updateMerchants()

                checkout_select_customer_spinner.selectItemByIndex(0)

                updateUI()
            }

            dialog.show()
        }

        checkout_payment_method_paid.setOnCheckedChangeListener { button, checked ->
            if (checked)
                CREATE_ORDER_MODEL.paymentMethod = button.text.toString()
        }

        checkout_payment_method_credit.setOnCheckedChangeListener { button, checked ->
            if (checked)
                CREATE_ORDER_MODEL.paymentMethod = button.text.toString()
        }

        checkout_finish_order.setOnSingleClickListener {
            // Finish order operation
            viewModel.postSales()
        }

        initSubscribers()
        updateRV()
        updateUI()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeResponse()
        viewModel.start()
    }

    private fun subscribeResponse() {
        viewModel.onMerchantsLoadedLiveData.observe(this) {
            updateMerchants()
        }

        viewModel.onSaleSubmittedLiveData.observe(this) {
            if (it) {
                CREATE_ORDER_MODEL.clearAll()
                flow.openOrderCompleted()
            }
        }
    }

    override fun onBackButtonPressed(): Boolean {
        CREATE_ORDER_MODEL.onOrderedProductsUpdatedLiveData.postValue(true)
        checkout_select_customer_spinner.dismiss()

        return super.onBackButtonPressed()
    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        checkout_loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    private fun updateMerchants() {
        checkout_select_customer_spinner.apply {
            setItems(viewModel.merchantNameList)
            setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newItem ->
                viewModel.merchantList[newIndex].apply {
                    CREATE_ORDER_MODEL.customer = this
                    CREATE_ORDER_MODEL.customerTinNumber = this.merchantTIN
                    checkout_tin_number.setText(this.merchantTIN)
                    checkout_vrn_number.setText(this.merchantVRN)

                    updateUI()
                }
            }

            CREATE_ORDER_MODEL.customer?.let {
                text = it.name
            }
        }
    }

    private fun updateRV() {
        val products = CREATE_ORDER_MODEL.productsToBeOrdered

        productsAdapter = CheckoutProductsRVAdapter(products) { position, item, type ->
            when (type) {
                ItemSelectionType.QTY -> updateQuantity(position, item)
                ItemSelectionType.PRICE_CATEGORY -> updatePriceCategory(position, item)
                ItemSelectionType.DISCOUNT -> updateDiscount(position, item)
                ItemSelectionType.DELETE -> deleteItem(position, item)
                else -> {}
            }
        }

        checkout_product_list.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = productsAdapter
        }
    }

    private fun updateQuantity(position: Int, product: ProductModel) {
        val availableStockAmount = viewModel.getAvailableStockAmount(product)

        val dialog = ProductQuantityDialog(requireActivity(), product, availableStockAmount, position) { position, product ->
            productsAdapter.notifyItemChanged(position)
            updateUI()
        }

        dialog.show()
    }

    private fun updatePriceCategory(position: Int, product: ProductModel) {
        val dialog = ProductPriceCategoryDialog(requireActivity(), product, position) { position, product ->
            productsAdapter.notifyItemChanged(position)
            updateUI()
        }

        dialog.show()
    }

    private fun updateDiscount(position: Int, product: ProductModel) {
        val dialog = ProductDiscountDialog(requireActivity(), product, position) { position, product ->
            productsAdapter.notifyItemChanged(position)
            updateUI()
        }

        dialog.show()
    }

    private fun deleteItem(position: Int, product: ProductModel) {
        val confirmDialog = AlertDialog.Builder(requireActivity())
        confirmDialog.apply {
            setTitle(R.string.warning_delete_product_item)

            setPositiveButton(R.string.delete) { dialog, which ->
                CREATE_ORDER_MODEL.remove(product);
                productsAdapter.notifyItemRemoved(position);
                updateUI()
            }

            setNegativeButton(android.R.string.cancel, null)

            show()
        }
    }

    private fun updateUI() {
        val totalPrice = CREATE_ORDER_MODEL.getTotalOrderedPrice()
        val totalDiscount = 0.0
        val totalVat = CREATE_ORDER_MODEL.getTotalVat(viewModel.taxInformation)
        val total = totalPrice - totalDiscount + totalVat

        checkout_subtotal.text = String.format("TZS %s", NumberFormat.getNumberInstance(Locale.US).format(totalPrice))
        checkout_discount.text = String.format("-TZS %s", NumberFormat.getNumberInstance(Locale.US).format(totalDiscount))
        checkout_total_vat.text = String.format("TZS %s", NumberFormat.getNumberInstance(Locale.US).format(totalVat))
        checkout_total.text = String.format("TZS %s", NumberFormat.getNumberInstance(Locale.US).format(total))
        checkout_total_price_label.text = String.format("TZS %s", NumberFormat.getNumberInstance(Locale.US).format(total))

        checkout_finish_order.isEnabled = CREATE_ORDER_MODEL.canFinishOrder()
    }
}