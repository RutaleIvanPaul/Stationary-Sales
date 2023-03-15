package io.ramani.ramaniStationary.data.home.models.response

import com.google.gson.annotations.SerializedName

data class ProductRemoteModel(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("archived")
    val archived: Boolean?,
    @SerializedName("imagePath")
    val imagePath: String?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("hasSecondaryUnitConversion")
    val hasSecondaryUnitConversion: Boolean?,
    @SerializedName("secondaryUnitConversion")
    val secondaryUnitConversion: Int?,
    @SerializedName("secondaryUnitName")
    val secondaryUnitName: String?,
    @SerializedName("vatCategory")
    val vatCategory: String?,
    @SerializedName("supplierId")
    val supplierId: String?,
    @SerializedName("supplierProductId")
    val supplierProductId: String?,
    @SerializedName("externalId")
    val externalId: String?,
    @SerializedName("productCategories")
    val productCategories: List<ProductCategoryRemoteModel>?,
)