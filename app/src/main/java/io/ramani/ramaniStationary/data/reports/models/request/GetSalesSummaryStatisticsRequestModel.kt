package io.ramani.ramaniStationary.data.reports.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetSalesSummaryStatisticsRequestModel(
    val companyId:String,
    val salesPersonUID:String,
    val page:Int,
    val startDate:String,
    val endDate:String,
) : Params
