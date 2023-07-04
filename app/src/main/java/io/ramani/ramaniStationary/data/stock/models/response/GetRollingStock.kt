package io.ramani.ramaniStationary.data.stock.models.response

import com.google.gson.annotations.SerializedName

data class GetRollingStock(

	@SerializedName("lastUpdated")
	val lastUpdated: String? = null,

	@SerializedName("salesPersonUID")
	val salesPersonUID: String? = null,

	@SerializedName("companyId")
	val companyId: String? = null,

	@SerializedName("_id")
	val id: String? = null,

	@SerializedName("products")
	val products: List<ProductsItem>? = null
)

data class ProductsItem(

	@SerializedName("quantity")
	val quantity: Int? = null,

	@SerializedName("productId")
	val productId: String? = null,

	@SerializedName("secondaryUnitQuantity")
	val secondaryUnitQuantity: Int? = null,

	@SerializedName("secondaryUnits")
	val secondaryUnits: String? = null,

	@SerializedName("_id")
	val id: String? = null,

	@SerializedName("units")
	val units: String? = null,

	@SerializedName("productName")
	val productName: String
)
