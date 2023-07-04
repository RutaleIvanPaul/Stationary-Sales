package io.ramani.ramaniStationary.data.auth.models

import com.google.gson.annotations.SerializedName

data class UserRemoteModel(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("phoneNumber")
    val phoneNumber: String = "",
    @SerializedName("token")
    val token: String = "",
    @SerializedName("companyId")
    val companyId: String = "",
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("timeZone")
    val timeZone: String = "",
    @SerializedName("roles")
    val roles: List<String> = listOf(),
    @SerializedName("permissions")
    val permissions: List<UserPermissionRemoteModel> = listOf()
)