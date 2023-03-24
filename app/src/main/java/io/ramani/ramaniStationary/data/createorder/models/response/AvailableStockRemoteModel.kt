package io.ramani.ramaniStationary.data.createorder.models.response

import com.google.gson.annotations.SerializedName

data class AvailableStockRemoteModel(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("companyId")
    val companyId: String?,
    @SerializedName("lastUpdated")
    val lastUpdated: String?,
    @SerializedName("products")
    val products: List<AvailableProductRemoteModel>?,
    @SerializedName("salesPersonUID")
    val salesPersonUID: String?,
    @SerializedName("__v")
    val v: Int?,
    @SerializedName("warehouseId")
    val warehouseId: String?
)
