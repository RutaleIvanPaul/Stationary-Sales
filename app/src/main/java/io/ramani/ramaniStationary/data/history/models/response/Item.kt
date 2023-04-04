package io.ramani.ramaniStationary.data.history.models.response



data class Item(
    val categoryId: String = "",
    val categoryName: String = "",
    val price: Double = 0.0,
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val supplierProductId: String = "",
    val units: String = "",
    val vatCategory: String = ""
)