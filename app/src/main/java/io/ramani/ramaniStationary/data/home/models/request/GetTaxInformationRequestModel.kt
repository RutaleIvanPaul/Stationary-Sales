package io.ramani.ramaniStationary.data.home.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetTaxInformationRequestModel(
    val userId:String
) : Params
