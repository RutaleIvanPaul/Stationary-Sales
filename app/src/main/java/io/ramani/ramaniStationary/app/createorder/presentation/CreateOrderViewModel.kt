package io.ramani.ramaniStationary.app.createorder.presentation

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import io.ramani.ramaniStationary.app.common.presentation.errors.PresentationError
import io.ramani.ramaniStationary.app.common.presentation.viewmodels.BaseViewModel
import io.ramani.ramaniStationary.data.common.prefs.PrefsManager
import io.ramani.ramaniStationary.data.createmerchant.models.request.RegisterMerchantRequestModel
import io.ramani.ramaniStationary.data.createorder.database.SaleJsonModel
import io.ramani.ramaniStationary.data.createorder.models.request.GetAvailableStockRequestModel
import io.ramani.ramaniStationary.data.createorder.models.request.SaleRequestModel
import io.ramani.ramaniStationary.data.database.RamaniDatabase
import io.ramani.ramaniStationary.data.home.models.request.GetMerchantRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetProductRequestModel
import io.ramani.ramaniStationary.data.home.models.request.GetTaxRequestModel
import io.ramani.ramaniStationary.domain.auth.manager.ISessionManager
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domain.base.SingleLiveEvent
import io.ramani.ramaniStationary.domain.base.v2.BaseSingleUseCase
import io.ramani.ramaniStationary.domain.createorder.model.AvailableProductModel
import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel
import io.ramani.ramaniStationary.domain.datetime.DateFormatter
import io.ramani.ramaniStationary.domain.entities.PagedList
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxInformationModel
import io.ramani.ramaniStationary.domain.home.model.TaxModel
import io.ramani.ramaniStationary.domainCore.presentation.language.IStringProvider
import io.ramani.ramaniStationary.domainCore.printer.PrintStatus
import io.ramani.ramaniStationary.domainCore.printer.PrinterHelper
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.NumberFormat
import java.util.*

class CreateOrderViewModel(
    application: Application,
    stringProvider: IStringProvider,
    sessionManager: ISessionManager,
    private val getTaxesUseCase: BaseSingleUseCase<PagedList<TaxModel>, GetTaxRequestModel>,
    private val getProductsUseCase: BaseSingleUseCase<PagedList<ProductModel>, GetProductRequestModel>,
    private val getMerchantsUseCase: BaseSingleUseCase<PagedList<MerchantModel>, GetMerchantRequestModel>,
    private val getAvailableStockUseCase: BaseSingleUseCase<List<AvailableStockModel>, GetAvailableStockRequestModel>,
    private val registerMerchantUseCase: BaseSingleUseCase<MerchantModel, RegisterMerchantRequestModel>,
    private val prefs: PrefsManager,
    val dateFormatter: DateFormatter,
    private val printerHelper: PrinterHelper,
    private val database: RamaniDatabase
) : BaseViewModel(application, stringProvider, sessionManager) {

    var userId = ""
    var companyId = ""
    var userModel: UserModel = UserModel()

    val merchantList = mutableListOf<MerchantModel>()
    val merchantNameList = mutableListOf<String>()
    val onMerchantsLoadedLiveData = SingleLiveEvent<List<MerchantModel>>()

    val productList = mutableListOf<ProductModel>()
    val onProductsLoadedLiveData = SingleLiveEvent<List<ProductModel>>()

    val taxesList = mutableListOf<TaxModel>()
    val onTaxesLoadedLiveData = SingleLiveEvent<List<TaxModel>>()

    val availableStockProductList = mutableListOf<AvailableProductModel>()
    val onAvailableStockProductsLoadedLiveData = SingleLiveEvent<List<AvailableProductModel>>()

    val onSaleSavedLoadedData = SingleLiveEvent<Long>()

    val onMerchantAddedLiveData = SingleLiveEvent<Pair<MerchantModel?, String>>()

    val isRestrictSalesByStockAssigned = prefs.userAccountDetails.restrictSalesByStockAssigned

    val taxInformation: TaxInformationModel
        get() = prefs.taxInformation

    @SuppressLint("CheckResult")
    override fun start(args: Map<String, Any?>) {
        sessionManager.getLoggedInUser().subscribeBy {
            userId = it.uuid
            companyId = it.companyId
            userModel = it

            getProducts()
            getMerchants()
            getAvailableStockList()
        }
    }

    @SuppressLint("CheckResult")
    fun getMerchants() {
        merchantList.clear()
        merchantNameList.clear()

        sessionManager.getLoggedInUser().subscribeBy {
            val single = getMerchantsUseCase.getSingle(GetMerchantRequestModel(false, companyId, "", "", true, 1))
            subscribeSingle(single, onSuccess = {
                merchantList.addAll(it.data.sortedByDescending { merchant -> merchant.updatedAt })

                merchantList.forEach { merchant ->
                    merchantNameList.add(merchant.name)
                }

                onMerchantsLoadedLiveData.postValue(merchantList)

            }, onError = {
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
            })
        }
    }

    @SuppressLint("CheckResult")
    fun getProducts() {
        productList.clear()

        sessionManager.getLoggedInUser().subscribeBy {
            val single = getProductsUseCase.getSingle(GetProductRequestModel(false, companyId, "", "",false, 1))
            subscribeSingle(single, onSuccess = {
                productList.addAll(it.data)
                onProductsLoadedLiveData.postValue(productList)

            }, onError = {
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
            })
        }
    }

    @SuppressLint("CheckResult")
    fun getTaxes() {
        taxesList.clear()

        sessionManager.getLoggedInUser().subscribeBy {
            val single = getTaxesUseCase.getSingle(GetTaxRequestModel(false, companyId, userId, "", 1))
            subscribeSingle(single, onSuccess = {
                taxesList.addAll(it.data)
                onTaxesLoadedLiveData.postValue(taxesList)
            }, onError = {
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
            })
        }
    }

    @SuppressLint("CheckResult")
    fun getAvailableStockList() {
        isLoadingVisible = true
        availableStockProductList.clear()

        sessionManager.getLoggedInUser().subscribeBy {
            val single = getAvailableStockUseCase.getSingle(GetAvailableStockRequestModel(userId))
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false

                it.forEach {
                    availableStockProductList.addAll(it.products)
                }

                onAvailableStockProductsLoadedLiveData.postValue(availableStockProductList)

            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
            })
        }
    }

    fun getAvailableStockAmount(product: ProductModel): Int {
        val availableStockProducts = availableStockProductList.filter { pp -> pp.productId == product.id }
        return if (availableStockProducts.isNotEmpty()) availableStockProducts.first().quantity else 0
    }

    @SuppressLint("CheckResult")
    fun saveSale() {
        isLoadingVisible = true

        sessionManager.getLoggedInUser().subscribeBy {
            userId = it.uuid
            companyId = it.companyId
            val userName = it.name
            val companyName = it.companyId
            val date = Date()
            val fullTimeStamp = dateFormatter.getTimeWithFormmatter(date, "dd MMM, yyyy HH:mm")
            val checkTime = dateFormatter.getTimeWithFormmatter(date, "HH:mm:ss")
            val deliveryDate = dateFormatter.getCalendarTimeWithDashes(date)
            val receiptCode = prefs.taxInformation.receiptCode
            val vrn = prefs.taxInformation.vrn

            val request = CREATE_ORDER_MODEL.createSaleRequestModel(date.time, companyId, companyName, userId, userName, fullTimeStamp, checkTime, deliveryDate, receiptCode, vrn)
            val requestJson = Gson().toJson(request)
            val saleJson = SaleJsonModel(date.time, false, requestJson)
            database.getSaleDao().insert(saleJson).subscribeBy { identify ->
                isLoadingVisible = false

                onSaleSavedLoadedData.postValue(identify)
            }
        }
    }

    fun getSaleRequest(saleIdentify: Long): SaleRequestModel {
        val saleJson = database.getSaleDao().getSale(saleIdentify)
        return Gson().fromJson(saleJson.request, SaleRequestModel::class.java)
    }

    fun updatePrintStatus(saleIdentify: Long, printStatus: String) {
        val saleJson = database.getSaleDao().getSale(saleIdentify)
        val sale = Gson().fromJson(saleJson.request, SaleRequestModel::class.java)
        sale.printStatus = printStatus
        saleJson.request = Gson().toJson(sale)
        database.getSaleDao().update(saleJson)
    }

    fun doPrintReceipt(saleIdentify: Long): PrintStatus {
        // Get tax information
        val taxInformation = prefs.taxInformation

        var receiptText = ""

        getSaleRequest(saleIdentify).let {
            val priceMap = calculatePrices(it, taxInformation)

            var totalTaxString = ""
            if (taxInformation.isVRNNotRegistered()) {
                totalTaxString = "${priceMap["total_cost_with_tax"]} TSH"
            } else {
                totalTaxString = "${it.totalCost} TSH"
            }

            receiptText = "\n*** START OF LEGAL RECEIPT***\n" +
                    "   ${taxInformation.name}\n" +
                    "TIN: ${taxInformation.tin}\n" +
                    "VRN: ${taxInformation.vrn}\n" +
                    "UIN: ${taxInformation.uin}\n" +
                    "RECEIPT NUMBER:  ${taxInformation.gc - 1}\n" +
                    "CUSTOMER NAME: ${it.buyerCompanyName}\n" +
                    "CUSTOMER ID: ${it.merchantTIN ?: "NOT REGISTERED"}\n" +
                    "CUSTOMER VRN: ${it.merchantVRN ?: "NOT REGISTERED"}\n" +
                    "RECEIPT DATE: ${it.fullActivityTimeStamp}\n" +
                    "RECEIPT TIME: ${it.checkInTime}\n" +
                    "===============================\n" +
                    priceMap["items"] +
                    "===============================\n" +
                    "TOTAL EXCL TAX: ${priceMap["total_cost_with_tax"]} TSH \n" +
                    "TOTAL VAT 18%: ${priceMap["total_vat"]} TSH \n" +
                    "TOTAL VAT E-EX: 0.00 TSH \n" +
                    "TOTAL INCL TAX: ${totalTaxString} \n" +
                    "   RECEIPT VERIFICATION CODE: \n" +
                    "               ${taxInformation.receiptCode}${taxInformation.gc - 1}" +
                    "\n\n--------------------------------\n\n"
        }

        var status = printText(receiptText)

        if (status.status) {
            createQRCodeBitmap(String.format("https://verify.tra.go.tz/%s%d", taxInformation.receiptCode, taxInformation.gc - 1))?.let {
                status = printBitmap(it)
            }

            if (status.status)
                status = printText("***END OF LEGAL RECEIPT***")
        }

        return status
    }

    private fun calculatePrices(sale: SaleRequestModel, tax: TaxInformationModel): Map<String, String> {
        var items = ""

        var total_cost_with_tax = 0.0
        var total_exempt = 0.0
        var total_vat = 0.0

        sale.createdOrders.first().items.forEach {
            val item_cost = it.quantity * it.price
            items += "${it.productName} ${it.quantity} x ${it.price} TSH = ${item_cost} TSH ${it.vatCategory}\n\n"

            total_cost_with_tax += item_cost

            if (it.vatCategory != "E") {
                if (!tax.isVRNNotRegistered()) {
                    //User's VRN is registered
                    total_vat += 0.18 * item_cost / 1.18
                }
            } else {
                //Item is Vat E
                total_exempt += item_cost
            }
        }

        val priceMap = HashMap<String, String>()
        priceMap["items"] = items
        priceMap["total_cost_with_tax"] = (total_cost_with_tax - total_vat).toString()
        priceMap["total_exempt"] = total_exempt.toString()
        priceMap["total_vat"] = total_vat.toString()

        return priceMap
    }

    @SuppressLint("CheckResult")
    fun registerMerchant(merchant: RegisterMerchantRequestModel) {
        isLoadingVisible = true

        sessionManager.getLoggedInUser().subscribeBy { user ->
            val single = registerMerchantUseCase.getSingle(merchant)
            subscribeSingle(single, onSuccess = {
                isLoadingVisible = false

                merchantList.add(0, it)
                merchantNameList.add(0, it.name)

                onMerchantAddedLiveData.postValue(Pair(first = it, second = ""))
            }, onError = {
                isLoadingVisible = false
                notifyErrorObserver(getErrorMessage(it), PresentationError.ERROR_TEXT)
                onMerchantAddedLiveData.postValue(Pair(first = null, second = getErrorMessage(it)))
            })
        }
    }

    fun printBitmap(bitmap: Bitmap): PrintStatus {
        val status = printerHelper.printBitmap(bitmap)
        if(!status.status) {
            notifyErrorObserver(status.error, PresentationError.ERROR_TEXT)
        }

        return status
    }

    fun printText(text: String): PrintStatus {
        val status = printerHelper.printText(text)
        if(!status.status){
            notifyErrorObserver(status.error, PresentationError.ERROR_TEXT)
        }

        return status
    }

    private fun createQRCodeBitmap(qrCodeInput: String?): Bitmap? {
        var result: BitMatrix? = null
        result = try {
            MultiFormatWriter().encode(qrCodeInput, BarcodeFormat.QR_CODE, 300, 300, null)
        } catch (e: WriterException) {
            e.printStackTrace()
            return null
        }
        val width: Int = result?.width ?: 300
        val height: Int = result?.height ?: 300
        val pixels = IntArray(width * height)
        for (heightIndex in 0 until height) {
            val offset = heightIndex * width
            for (widthIndex in 0 until width) {
                if (result != null) {
                    pixels[offset + widthIndex] =
                        if (result.get(widthIndex, heightIndex)) Color.BLACK else Color.WHITE
                }
            }
        }
        val qrBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        qrBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return qrBitmap
    }

    fun getFormattedAmount(amount: Int): String = NumberFormat.getNumberInstance(Locale.US).format(amount)

    class Factory(
        private val application: Application,
        private val stringProvider: IStringProvider,
        private val sessionManager: ISessionManager,
        private val getTaxesUseCase: BaseSingleUseCase<PagedList<TaxModel>, GetTaxRequestModel>,
        private val getProductsUseCase: BaseSingleUseCase<PagedList<ProductModel>, GetProductRequestModel>,
        private val getMerchantsUseCase: BaseSingleUseCase<PagedList<MerchantModel>, GetMerchantRequestModel>,
        private val getAvailableStockUseCase: BaseSingleUseCase<List<AvailableStockModel>, GetAvailableStockRequestModel>,
        private val registerMerchantUseCase: BaseSingleUseCase<MerchantModel, RegisterMerchantRequestModel>,
        private val prefs: PrefsManager,
        private val dateFormatter: DateFormatter,
        private val printerHelper: PrinterHelper,
        private val database: RamaniDatabase
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateOrderViewModel::class.java)) {
                return CreateOrderViewModel(
                    application,
                    stringProvider,
                    sessionManager,
                    getTaxesUseCase, getProductsUseCase, getMerchantsUseCase,
                    getAvailableStockUseCase,
                    registerMerchantUseCase,
                    prefs,
                    dateFormatter,
                    printerHelper,
                    database
                ) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }
}