package io.ramani.ramaniStationary.data.credit.mappers

import io.ramani.ramaniStationary.data.createmerchant.models.request.MerchantRouteModel
import io.ramani.ramaniStationary.data.credit.models.response.MerchantRouteRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper

class MerchantRouteRemoteMapper() : ModelMapper<MerchantRouteRemoteModel, MerchantRouteModel> {
    override fun mapFrom(from: MerchantRouteRemoteModel): MerchantRouteModel =
        MerchantRouteModel.Builder()
                .routeId(from.routeId ?: "")
                .routeName(from.routeName ?: "")
                .build()

    override fun mapTo(to: MerchantRouteModel): MerchantRouteRemoteModel =
        MerchantRouteRemoteModel(
                    to.routeId,
                    to.routeName
                )
}