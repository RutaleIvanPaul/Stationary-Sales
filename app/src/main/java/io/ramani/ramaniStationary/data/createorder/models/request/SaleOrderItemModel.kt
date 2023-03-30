package io.ramani.ramaniStationary.data.createorder.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class SaleOrderItemModel (
    val productId: String,
    val productName: String,
    val price: Double,
    val quantity: Double,
    val vatCategory: String,
    val units: String,
    val supplierProductId: String,
    val categoryId: String,
    val categoryName: String
) : Params