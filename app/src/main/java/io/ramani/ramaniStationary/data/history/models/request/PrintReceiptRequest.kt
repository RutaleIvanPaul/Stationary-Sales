package io.ramani.ramaniStationary.data.history.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params
import retrofit2.http.Query

data class PrintReceiptRequest(
    val id: String,
    val uin: String,
    val  sellerName: String
): Params
