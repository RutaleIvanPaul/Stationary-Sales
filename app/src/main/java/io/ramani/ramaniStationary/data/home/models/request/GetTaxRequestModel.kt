package io.ramani.ramaniStationary.data.home.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetTaxRequestModel(
    val fromRemote:Boolean,
    val companyId:String,
    val userId:String,
    val date:String,
    val page:Int
) : Params
