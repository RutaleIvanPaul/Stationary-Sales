package io.ramani.ramaniStationary.data.credit.models.response

import com.google.gson.annotations.SerializedName

data class MetaDataItemRemoteModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("value")
    val value: String?,
    @SerializedName("fieldId")
    val fieldId: String?
)
