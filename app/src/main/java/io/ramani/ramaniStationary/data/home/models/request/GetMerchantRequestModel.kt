package io.ramani.ramaniStationary.data.home.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetMerchantRequestModel(
    val date:String,
    val isActive:Boolean,
    val page:Int
) : Params
