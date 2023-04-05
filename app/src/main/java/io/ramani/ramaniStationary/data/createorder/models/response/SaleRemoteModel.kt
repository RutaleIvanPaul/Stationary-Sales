package io.ramani.ramaniStationary.data.createorder.models.response

import com.google.gson.annotations.SerializedName

data class SaleRemoteModel(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("activityDate")
    val activityDate: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String
)
