package io.ramani.ramaniStationary.data.home.mappers

import io.ramani.ramaniStationary.data.home.models.response.MerchantMemberRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.home.model.MerchantMemberModel

class MerchantMemberRemoteMapper : ModelMapper<MerchantMemberRemoteModel, MerchantMemberModel> {

    override fun mapFrom(from: MerchantMemberRemoteModel): MerchantMemberModel =
        MerchantMemberModel.Builder()
            .name(from.name ?: "")
            .phoneNumber(from.phoneNumber ?: "")
            .build()

    override fun mapTo(to: MerchantMemberModel): MerchantMemberRemoteModel =
        MerchantMemberRemoteModel(
                to.name,
                to.phoneNumber
            )
}