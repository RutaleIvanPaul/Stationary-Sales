package io.ramani.ramaniStationary.data.home.models.response

import com.google.gson.annotations.SerializedName
import io.ramani.ramaniStationary.domain.home.model.MerchantMemberModel

data class MerchantMemberRemoteModel(
    @SerializedName("name")
    val name: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
)