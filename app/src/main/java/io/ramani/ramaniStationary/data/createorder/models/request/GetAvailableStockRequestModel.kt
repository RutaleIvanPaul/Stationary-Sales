package io.ramani.ramaniStationary.data.createorder.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetAvailableStockRequestModel(
    val salesPersonUID: String
): Params
