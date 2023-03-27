package io.ramani.ramaniStationary.data.stock.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class AvailableStockRequestModel (
    var salesPersonUID: String
        ): Params