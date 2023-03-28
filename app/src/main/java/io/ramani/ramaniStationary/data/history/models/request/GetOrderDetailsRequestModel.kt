package io.ramani.ramaniStationary.data.history.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetOrderDetailsRequestModel(
    val orderId: String
): Params