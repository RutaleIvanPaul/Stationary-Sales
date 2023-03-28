package io.ramani.ramaniStationary.data.history.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetZReportRequestModel(
    val uin: String,
    val companyId: String,
    val startDate: String,
    val endDate: String,
    val sellerName: String
): Params
