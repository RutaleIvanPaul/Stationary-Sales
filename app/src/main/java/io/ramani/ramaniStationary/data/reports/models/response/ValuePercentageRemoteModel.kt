package io.ramani.ramaniStationary.data.reports.models.response

import com.google.gson.annotations.SerializedName

data class ValuePercentageRemoteModel(
    @SerializedName("value")
    val value: Double?,
    @SerializedName("percentageIncrease")
    val percentageIncrease: Double?
)
