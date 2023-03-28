package io.ramani.ramaniStationary.data.history.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetXReportRequestModel (
    val uin: String,
    val date: String,
    val sellerName: String,
    val companyId: String
        ): Params
