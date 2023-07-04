package io.ramani.ramaniStationary.data.home.models.response

import com.google.gson.annotations.SerializedName

data class ProductCategoryRemoteModel(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("categoryId")
    val categoryId: String?,
    @SerializedName("unitPrice")
    val unitPrice: Double?
)