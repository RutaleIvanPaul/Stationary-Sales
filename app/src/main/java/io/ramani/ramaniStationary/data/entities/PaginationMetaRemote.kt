package io.ramani.ramaniStationary.data.entities

import com.google.gson.annotations.SerializedName

data class PaginationMetaRemote(
    @SerializedName("totalSize") val total: Int? = 0,
    @SerializedName("requestedLimit") val perPage: Int? = 0,
    @SerializedName("requestedPage") val currentPage: Int? = 0,
    @SerializedName("nextCursor") val nextCursor: String? = "",
    @SerializedName("hasNext") val hasNext: Boolean? = false,
    @SerializedName("totalPages") val totalPages: Int? = 0
)