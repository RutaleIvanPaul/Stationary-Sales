package io.ramani.ramaniStationary.data.history.models.response



data class OrderDetailsResponse(
    val buyerId: String = "",
    val buyerName: String = "",
    val order: Order = Order(),
    val printStatus: String = "",
    val salesPersonUID: String = "",
    val sellerId: String = "",
    val sellerName: String = ""
)