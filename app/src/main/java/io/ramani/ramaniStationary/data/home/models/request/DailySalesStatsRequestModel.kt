package io.ramani.ramaniStationary.data.home.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class DailySalesStatsRequestModel(
    val date:String,
    val companyId:String,
    val page:Int,
    val size:Int,
    val startDate:String,
    val endDate:String
) : Params
