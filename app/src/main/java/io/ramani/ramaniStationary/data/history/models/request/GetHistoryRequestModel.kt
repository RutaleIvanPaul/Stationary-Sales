package io.ramani.ramaniStationary.data.history.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetHistoryRequestModel(
    val userId: String,
    val day: Int,
    val month: String,
    val year: Int
):Params