package io.ramani.ramaniStationary.data.credit.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class UpdateOrderPaymentStatusRequestModel(
    val orderId: String,
    val userId: String,
    val oldStatus: String,
    val paymentStatus: Int,
    val timeStamp: String,
): Params
