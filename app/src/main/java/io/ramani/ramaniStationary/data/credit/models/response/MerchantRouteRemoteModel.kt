package io.ramani.ramaniStationary.data.credit.models.response

import com.google.gson.annotations.SerializedName

data class MerchantRouteRemoteModel(
    @SerializedName("routeId")
    val routeId: String?,
    @SerializedName("routeName")
    val routeName: String?
)
