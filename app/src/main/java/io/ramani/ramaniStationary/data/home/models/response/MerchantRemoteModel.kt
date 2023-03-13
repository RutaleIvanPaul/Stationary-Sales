package io.ramani.ramaniStationary.data.home.models.response

import com.google.gson.annotations.SerializedName
import io.ramani.ramaniStationary.domain.home.model.MerchantMemberModel

data class MerchantRemoteModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("isActive")
    val isActive: Boolean?,
    @SerializedName("gps")
    val gps: String?,
    @SerializedName("salesPersonUID")
    val salesPersonUID: String?,
    @SerializedName("salesPersonName")
    val salesPersonName: String?,
    @SerializedName("members")
    val members: List<MerchantMemberRemoteModel>?,
    @SerializedName("merchantTIN")
    val merchantTIN: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("creditLimit")
    val creditLimit: Int?,
    @SerializedName("merchantType")
    val merchantType: String?
)