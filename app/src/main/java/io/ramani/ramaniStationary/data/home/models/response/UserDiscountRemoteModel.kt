package io.ramani.ramaniStationary.data.home.models.response

import com.google.gson.annotations.SerializedName

data class UserDiscountRemoteModel(
    @SerializedName("name")
    val name: String?,
    @SerializedName("companyId")
    val companyId: String?,
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("dateCreated")
    val dateCreated: String?
)