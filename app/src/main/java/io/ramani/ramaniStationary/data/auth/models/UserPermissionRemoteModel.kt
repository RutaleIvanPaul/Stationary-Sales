package io.ramani.ramaniStationary.data.auth.models

import com.google.gson.annotations.SerializedName

data class UserPermissionRemoteModel(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("permission")
    val permission: String = "",
    @SerializedName("lastUpdated")
    val lastUpdated: String = "",
    @SerializedName("authoriser")
    val authoriser: String = "",
    @SerializedName("isActive")
    val isActive: Boolean = false,
)