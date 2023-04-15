package io.ramani.ramaniStationary.data.createorder.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class SaleOrderModel (
    val salesPersonUuid: String,
    val buyerId: String,
    val sellerId: String,
    val orderStatus: Double,
    val orderDate: String,
    val deliveryDate: String,
    val comment: String,
    val totalCost: Double,
    val deliveryStatus: Double,
    val paymentStatus: Double,
    val items: List<SaleOrderItemModel>
) : Params