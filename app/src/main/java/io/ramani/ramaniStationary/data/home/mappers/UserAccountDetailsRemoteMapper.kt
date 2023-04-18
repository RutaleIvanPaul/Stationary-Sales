package io.ramani.ramaniStationary.data.home.mappers

import io.ramani.ramaniStationary.data.home.models.response.UserAccountDetailsRemoteModel
import io.ramani.ramaniStationary.data.home.models.response.UserDiscountRemoteModel
import io.ramani.ramaniStationary.domain.home.model.UserAccountDetailsModel
import io.ramani.ramaniStationary.domain.home.model.UserDiscountModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper

class UserAccountDetailsRemoteMapper(
    private val discountRemoteMapper: ModelMapper<UserDiscountRemoteModel, UserDiscountModel>,
) : ModelMapper<UserAccountDetailsRemoteModel, UserAccountDetailsModel> {

    override fun mapFrom(from: UserAccountDetailsRemoteModel): UserAccountDetailsModel {
        val discounts: ArrayList<UserDiscountModel> = ArrayList()
        from.discounts?.forEach {
            discounts.add(discountRemoteMapper.mapFrom(it))
        }

        return UserAccountDetailsModel.Builder()
                .name(from.name ?: "")
                .accountType(from.accountType ?: "")
                .restrictSalesByStockAssigned(from.restrictSalesByStockAssigned ?: false)
                .discounts(discounts)
                .build()
    }

    override fun mapTo(to: UserAccountDetailsModel): UserAccountDetailsRemoteModel {
        val discounts: ArrayList<UserDiscountRemoteModel> = ArrayList()
        to.discounts.forEach {
            discounts.add(discountRemoteMapper.mapTo(it))
        }

        return UserAccountDetailsRemoteModel(
                to.name,
                to.accountType,
                to.restrictSalesByStockAssigned,
                discounts
            )
    }
}