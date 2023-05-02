package io.ramani.ramaniStationary.data.credit.models.request

import io.ramani.ramaniStationary.domain.base.v2.Params

data class GetLocationsRequestModel(
    val sellerId: String,
    val invalidateCache: Boolean,
    val page: Int
): Params
