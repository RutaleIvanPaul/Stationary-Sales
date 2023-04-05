package io.ramani.ramaniStationary.data.createmerchant.models.response

import com.google.gson.annotations.SerializedName

data class NameValueRemoteModel(
    @SerializedName("name")
    val name: String?,
    @SerializedName("value")
    val value: String?
)
