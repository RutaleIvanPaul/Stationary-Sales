package io.ramani.ramaniStationary.data.home.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetProductRequestModel(
    val companyId:String,
    val startDate:String,
    val endDate: String,
    val archived:Boolean,
    val page:Int
) : Params
