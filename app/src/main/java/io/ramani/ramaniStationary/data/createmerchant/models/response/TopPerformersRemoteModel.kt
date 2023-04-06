package io.ramani.ramaniStationary.data.createmerchant.models.response

import com.google.gson.annotations.SerializedName

data class TopPerformersRemoteModel(
    @SerializedName("topSalespeople")
    val topSalespeople: List<NameValueRemoteModel>?,
    @SerializedName("topMerchants")
    val topMerchants: List<NameValueRemoteModel>?
)
