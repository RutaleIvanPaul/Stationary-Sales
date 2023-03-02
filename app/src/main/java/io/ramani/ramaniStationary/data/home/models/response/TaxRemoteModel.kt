package io.ramani.ramaniStationary.data.home.models.response

import com.google.gson.annotations.SerializedName

data class TaxRemoteModel(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("NAME")
    val name: String = "",
    @SerializedName("TIN")
    val tin: String = "",
    @SerializedName("UIN")
    val uin: String = "",
    @SerializedName("VRN")
    val vrn: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)