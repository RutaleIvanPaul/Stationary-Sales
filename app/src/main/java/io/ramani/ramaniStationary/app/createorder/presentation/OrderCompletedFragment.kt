package io.ramani.ramaniStationary.app.createorder.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.auth.flow.AuthFlow
import io.ramani.ramaniStationary.app.auth.flow.AuthFlowController
import io.ramani.ramaniStationary.app.common.presentation.dialogs.errorDialog
import io.ramani.ramaniStationary.app.common.presentation.extensions.setOnSingleClickListener
import io.ramani.ramaniStationary.app.common.presentation.fragments.BaseFragment
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.app.createorder.flow.CreateOrderFlow
import io.ramani.ramaniStationary.app.createorder.flow.CreateOrderFlowController
import io.ramani.ramaniStationary.domainCore.printer.Manufacturer
import kotlinx.android.synthetic.main.fragment_order_complete.*
import kotlinx.android.synthetic.main.layout_print_info.*
import org.kodein.di.generic.factory
import java.text.NumberFormat
import java.util.*

private const val ARG_PARAM_SALE_IDENTIFY = "saleIdentify"

class OrderCompletedFragment : BaseFragment() {
    companion object {
        fun newInstance(saleIdentify: Long) =
            OrderCompletedFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM_SALE_IDENTIFY, saleIdentify)
                }
            }
    }

    private val viewModelProvider: (Fragment) -> CreateOrderViewModel by factory()
    private lateinit var viewModel: CreateOrderViewModel
    override val baseViewModel: BaseViewModel?
        get() = viewModel

    private lateinit var flow: CreateOrderFlow
    private lateinit var authFlow: AuthFlow

    override fun getLayoutResId(): Int = R.layout.fragment_order_complete

    private var saleIdentify: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider(this)
        setToolbarTitle("")
    }

    override fun initView(view: View?) {
        super.initView(view)

        arguments?.let {
            saleIdentify = it.getLong(ARG_PARAM_SALE_IDENTIFY)
        }

        flow = CreateOrderFlowController(baseActivity!!, R.id.main_fragment_container)
        authFlow = AuthFlowController(baseActivity!!, R.id.main_fragment_container)

        fragment_order_complete_new_sale.setOnSingleClickListener {
            //flow.openAddItems()
            authFlow.openMainNav()
        }

        fragment_order_complete_print_receipt.setOnSingleClickListener {
            var canPrint = true

            // If printer is bluetooth printer, then check bluetooth permission
            if (Build.MANUFACTURER.equals(Manufacturer.MobiIot.name) || Build.MANUFACTURER.equals(Manufacturer.MobiWire.name)) {
                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                    requireBluetoothPermission()
                    canPrint = false
                }
            }

            if (canPrint) {
                printOrderInfo()
            }
        }

        preparePrintInfo()

        initSubscribers()
    }

    private fun initSubscribers() {
        subscribeLoadingVisible(viewModel)
        subscribeLoadingError(viewModel)
        subscribeError(viewModel)
        observerError(viewModel, this)
        subscribeResponse()
        //viewModel.start()
    }

    private fun subscribeResponse() {

    }

    override fun setLoadingIndicatorVisible(visible: Boolean) {
        super.setLoadingIndicatorVisible(visible)
        //loader.visible(visible)
    }

    override fun showError(error: String) {
        super.showError(error)
        errorDialog(error)
    }

    override fun onBackButtonPressed(): Boolean {
        return true
    }

    private fun printOrderInfo() {
        /*
        val printView = print_scrollview
        val bitmap = Bitmap.createBitmap(printView.width, printView.getChildAt(0).height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = printView.background
        if (bgDrawable!=null){
            bgDrawable.draw(canvas)
        }
        else{
            canvas.drawColor(Color.WHITE)
        }
        printView.draw(canvas)

        if (viewModel.printBitmap(bitmap).status)
            flow.openPrintSuccessful()
        */

        if (viewModel.doPrintReceipt(saleIdentify).status) {
            viewModel.updatePrintStatus(saleIdentify, "Printed")

            flow.openPrintSuccessful()
        }
    }

    private fun preparePrintInfo() {
        viewModel.getSaleRequest(saleIdentify).let {
            print_info_tin_tv.text = it.merchantTIN ?: ""
            print_info_vrn_tv.text = it.merchantVRN ?: "*NOREGISTERED*"
            print_info_uni_tv.text = ""
            print_info_receipt_number_tv.text = ""

            print_info_sub_total_tv.text = String.format("%s %s", viewModel.currency.uppercase(), NumberFormat.getNumberInstance(Locale.US).format(it.totalCost))
            print_info_total_tv.text = String.format("%s %s", viewModel.currency.uppercase(), NumberFormat.getNumberInstance(Locale.US).format(it.totalCost))
            print_info_date_tv.text = it.fullActivityTimeStamp
            print_info_time_tv.text = it.checkInTime
        }
    }

    private fun requireBluetoothPermission() {
        ActivityCompat.requestPermissions(requireActivity(), BLE_PERMISSIONS, 1000)
    }

    private val BLE_PERMISSIONS = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}