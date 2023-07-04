package io.ramani.ramaniStationary.data.createmerchant.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetTopPerformersRequestModel(
    val companyId: String,
    val salesPersonUID: String,
    val startDate: String,
    val endDate: String,
    val size: Int = 1000
): Params
