package io.ramani.ramaniStationary.data.home.mappers

import io.ramani.ramaniStationary.data.home.models.response.UserDiscountRemoteModel
import io.ramani.ramaniStationary.domain.home.model.UserDiscountModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper

class UserDiscountRemoteMapper : ModelMapper<UserDiscountRemoteModel, UserDiscountModel> {
    override fun mapFrom(from: UserDiscountRemoteModel): UserDiscountModel =
        UserDiscountModel.Builder()
            .name(from.name ?: "")
            .companyId(from.companyId ?: "")
            .amount(from.amount ?: 0.0)
            .dateCreated(from.dateCreated ?: "")
            .build()

    override fun mapTo(to: UserDiscountModel): UserDiscountRemoteModel =
        UserDiscountRemoteModel(
            to.name,
            to.companyId,
            to.amount,
            to.dateCreated
        )
}