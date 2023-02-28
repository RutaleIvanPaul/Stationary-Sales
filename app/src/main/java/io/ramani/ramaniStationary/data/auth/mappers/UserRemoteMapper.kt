package io.ramani.ramaniStationary.data.auth.mappers

import io.ramani.ramaniStationary.data.auth.models.UserRemoteModel
import io.ramani.ramaniStationary.domain.auth.model.UserModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper

class UserRemoteMapper : ModelMapper<UserRemoteModel, UserModel> {
    override fun mapFrom(from: UserRemoteModel): UserModel =
        UserModel.Builder()
            .uuid(from.userId)
            .accountType(from.accountType)
            .companyId(from.companyId)
            .companyName(from.companyName)
            .companyType(from.companyType)
            .currency(from.currency)
            .fcmToken(from.fcmToken)
            .hasSeenSFAOnboarding(from.hasSeenSFAOnboarding)
            .isAdmin(from.isAdmin)
            .phoneNumber(from.phoneNumber)
            .name(from.userName)
            .timeZone(from.timeZone)
            .token(from.token)
            .build()

    override fun mapTo(to: UserModel): UserRemoteModel =
        UserRemoteModel(
            to.fcmToken,
            to.token,
            to.accountType,
            to.companyId,
            to.companyName,
            to.companyType,
            to.userName,
            to.phoneNumber,
            to.uuid,
            to.isAdmin,
            to.hasSeenSFAOnboarding,
            to.currency,
            to.timeZone
        )
}