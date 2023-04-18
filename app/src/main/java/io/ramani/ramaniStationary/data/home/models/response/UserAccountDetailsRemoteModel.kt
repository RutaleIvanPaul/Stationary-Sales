package io.ramani.ramaniStationary.data.home.models.response

import com.google.gson.annotations.SerializedName

data class UserAccountDetailsRemoteModel(
    @SerializedName("name")
    val name: String?,
    @SerializedName("accountType")
    val accountType: String?,
    @SerializedName("restrictSalesByStockAssigned")
    val restrictSalesByStockAssigned: Boolean?,
    @SerializedName("discounts")
    val discounts: List<UserDiscountRemoteModel>?
)