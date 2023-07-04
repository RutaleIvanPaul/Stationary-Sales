package io.ramani.ramaniStationary.data.home.mappers

import io.ramani.ramaniStationary.data.home.models.response.MerchantMemberRemoteModel
import io.ramani.ramaniStationary.data.home.models.response.MerchantRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.home.model.MerchantMemberModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel

class MerchantRemoteMapper(
    private val merchantMemberRemoteMapper: ModelMapper<MerchantMemberRemoteModel, MerchantMemberModel>,
) : ModelMapper<MerchantRemoteModel, MerchantModel> {

    override fun mapFrom(from: MerchantRemoteModel): MerchantModel {
        val members: ArrayList<MerchantMemberModel> = ArrayList()
        for (member in from.members ?: listOf()) {
            members.add(merchantMemberRemoteMapper.mapFrom(member))
        }

        return MerchantModel.Builder()
                .id(from.id ?: "")
                .name(from.name ?: "")
                .isActive(from.isActive ?: true)
                .gps(/* from.gps ?: */ "0, 0")
                .salesPersonUID(from.salesPersonUID ?: "")
                .salesPersonName(from.salesPersonName ?: "")
                .members(members)
                .merchantTIN(from.merchantTIN ?: "")
                .merchantVRN(from.merchantVRN ?: "")
                .updatedAt(from.updatedAt ?: "")
                .city(from.city ?: "")
                .creditLimit(from.creditLimit ?: 0)
                .merchantType(from.merchantType ?: "")
                .build()
    }

    override fun mapTo(to: MerchantModel): MerchantRemoteModel {
        val members: ArrayList<MerchantMemberRemoteModel> = ArrayList()
        for (member in to.members) {
            members.add(merchantMemberRemoteMapper.mapTo(member))
        }

        return MerchantRemoteModel(
                to.id,
                to.name,
                to.isActive,
                /* to.gps,*/
                to.salesPersonUID,
                to.salesPersonName,
                members,
                to.merchantTIN,
                to.merchantVRN,
                to.updatedAt,
                to.city,
                to.creditLimit,
                to.merchantType
            )
    }
}