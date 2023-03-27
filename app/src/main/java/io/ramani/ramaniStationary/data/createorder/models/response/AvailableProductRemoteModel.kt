package io.ramani.ramaniStationary.data.createorder.models.response

import com.google.gson.annotations.SerializedName

data class AvailableProductRemoteModel(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("productId")
    val productId: String?,
    @SerializedName("productName")
    val productName: String?,
    @SerializedName("quantity")
    var quantity: Int?,
    @SerializedName("secondaryUnitConversion")
    val secondaryUnitConversion: Int?,
    @SerializedName("secondaryUnitQuantity")
    val secondaryUnitQuantity: Int?,
    @SerializedName("secondaryUnits")
    val secondaryUnits: String?,
    @SerializedName("units")
    val units: String?,
    @SerializedName("supplierProductId")
    val supplierProductId: String?
)