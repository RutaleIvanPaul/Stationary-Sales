package io.ramani.ramaniStationary.data.createorder.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class SaleRequestModel (
    val sellerCompanyId: String,
    val sellerCompanyName: String,
    val buyerCompanyId: String?,
    val buyerCompanyName: String,
    val salespersonName: String,
    val salespersonUID: String,
    val fullActivityTimeStamp: String,
    val checkInTime: String,
    val checkOutTime: String,
    val merchantTIN: String?,
    val merchantVRN: String?,
    val createdOrders: List<SaleOrderModel>,
    val notes: String,
    val totalCost: Int,

    // Those parameter always null, but required by backend
    val zoneId: String = "zoneId",
    val checkInGPS: String = "0.00, 0.00",
    val checkOutGPS: String = "0.00, 0.00",
    val hasNewLocation:Boolean = false,
    val damagedProducts: List<String> = mutableListOf(),
    val merchantsToUpdate: List<String> = mutableListOf(),
    val customerFormsAnswers: List<String> = mutableListOf(),
    val areThereMerchantsToUpdate: Boolean = false,
    val reregisteredLocation: Boolean = false,
    val hasNewMerchantTIN: Boolean = false,
    val batteryLife: Double = 100.0,
    val photoUrls: List<String> = mutableListOf(),
    val VRN: String = "",
    val RECEIPTCODE: String = ""
) : Params {

    override fun toString(): String {
        return "SalesActivity{" +
                "sellerCompanyId='" + sellerCompanyId + '\'' +
                ", buyerCompanyId='" + buyerCompanyId + '\'' +
                ", buyerCompanyName='" + buyerCompanyName + '\'' +
                ", sellerCompanyName='" + sellerCompanyName + '\'' +
                ", salespersonName='" + salespersonName + '\'' +
                ", checkInTime='" + checkInTime + '\'' +
                ", checkOutTime='" + checkOutTime + '\'' +
                ", notes='" + notes + '\'' +
                ", fullActivityTimeStamp='" + fullActivityTimeStamp + '\'' +
                ", salespersonUID='" + salespersonUID + '\'' +
                ", merchantTIN='" + merchantTIN + '\'' +
                ", merchantVRN='" + merchantVRN + '\'' +
                //", createdOrders=" + createdOrders +
                '}'
    }
}
