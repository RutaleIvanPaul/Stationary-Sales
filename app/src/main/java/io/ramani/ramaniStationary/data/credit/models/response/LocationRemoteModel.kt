package io.ramani.ramaniStationary.data.credit.models.response

import com.google.gson.annotations.SerializedName
import io.ramani.ramaniStationary.data.createmerchant.models.request.MerchantRouteModel
import io.ramani.ramaniStationary.data.createmerchant.models.request.MetaDataItem
import io.ramani.ramaniStationary.domain.credit.model.CreditOrdersModel

data class LocationRemoteModel(
    @SerializedName("name")
    val name: String?,
    @SerializedName("salesPersonName")
    val salesPersonName: String?,
    @SerializedName("salesPersonUuid")
    val salesPersonUuid: String?,
    @SerializedName("creditOrders")
    val creditOrders: CreditOrdersModel?,
    @SerializedName("maxCredit")
    val maxCredit: Double?,
    @SerializedName("memberNumber")
    var memberNumber: String?,
    @SerializedName("merchantId")
    val merchantId: String?,
    @SerializedName("merchantTIN")
    val merchantTIN: String?,
    @SerializedName("merchantVRN")
    val merchantVRN: String?,
    @SerializedName("merchantType")
    val merchantType: String?,
    @SerializedName("merchantStatus")
    val merchantStatus: String?,
    @SerializedName("routes")
    val routes: List<MerchantRouteRemoteModel>?,
    @SerializedName("metaData")
    val metaData: List<MetaDataItemRemoteModel>?,
    @SerializedName("gps")
    val gps: String?,
    @SerializedName("isActive")
    val isActive: Boolean?
)
