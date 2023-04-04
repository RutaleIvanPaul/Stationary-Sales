package io.ramani.ramaniStationary.data.history.models.response



data class Order(
    val amendments: List<Any> = listOf(),
    val comment: String = "",
    val deliveryDate: String = "",
    val deliveryStatus: String = "",
    val discountRewardItems: List<Any> = listOf(),
    val id: String = "",
    val items: List<Item> = listOf(),
    val orderDate: String = "",
    val orderStatus: String = "",
    val paymentStatus: String = "",
    val payments: List<Any> = listOf(),
    val qtyRewardItems: List<Any> = listOf(),
    val totalCost: Double = 0.0
)