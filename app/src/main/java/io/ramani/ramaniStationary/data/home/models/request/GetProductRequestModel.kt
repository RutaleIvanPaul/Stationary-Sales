package io.ramani.ramaniStationary.data.home.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetProductRequestModel(
    val date:String,
    val archived:Boolean,
    val page:Int
) : Params
