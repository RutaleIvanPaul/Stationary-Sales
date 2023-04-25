package io.ramani.ramaniStationary.data.credit.models.response

import com.google.gson.annotations.SerializedName

data class CreditOrdersRemoteModel(
    @SerializedName("outstandingCredit")
    val outstandingCredit: Double?,
    @SerializedName("unpaidOrderIds")
    val unpaidOrderIds: List<String>?
)
