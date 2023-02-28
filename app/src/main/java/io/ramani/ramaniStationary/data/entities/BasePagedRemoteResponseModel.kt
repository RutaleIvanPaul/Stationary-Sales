package io.ramani.ramaniStationary.data.entities

import com.google.gson.annotations.SerializedName

data class BasePagedRemoteResponseModel<T>(
    @SerializedName("response")
    var data: T? = null,
    @SerializedName("requestedLimit") var limit: Int? = null,
    @SerializedName("requestedPage") var page: Int? = null,
    @SerializedName("totalPages") var totalPages: Int? = null,
    @SerializedName("totalSize") var totalDocsSize: Int? = null,
    @SerializedName("hasNext") var hasNextPage: Boolean? = null,
)