package io.ramani.ramaniStationary.data.home.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetTaxRequestModel(
    val date:String,
    val companyId:String,
    val userId:String,
    val page:Int,
    val size:Int
) : Params
