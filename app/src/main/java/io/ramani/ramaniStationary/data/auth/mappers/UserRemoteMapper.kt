package io.ramani.ramaniStationary.data.auth.mappers

import io.ramani.ramaniStationary.data.auth.models.UserRemoteModel
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper

class UserRemoteMapper : ModelMapper<UserRemoteModel, UserModel> {
    override fun mapFrom(from: UserRemoteModel): UserModel =
        UserModel.Builder()
            .uuid(from.id)
            .name(from.name)
            .phoneNumber(from.phoneNumber)
            .token(from.token)
            .companyId(from.companyId)
            .currency(from.currency)
            .timeZone(from.timeZone)
            .build()

    override fun mapTo(to: UserModel): UserRemoteModel =
        UserRemoteModel(
            to.uuid,
            to.name,
            to.phoneNumber,
            to.token,
            to.companyId,
            to.currency,
            to.timeZone
        )
}