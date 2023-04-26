package io.ramani.ramaniStationary.data.credit.mappers

import io.ramani.ramaniStationary.data.credit.models.response.CreditOrdersRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.credit.model.CreditOrdersModel

class CreditOrdersRemoteMapper() : ModelMapper<CreditOrdersRemoteModel, CreditOrdersModel> {
    override fun mapFrom(from: CreditOrdersRemoteModel): CreditOrdersModel =
        CreditOrdersModel.Builder()
                .outstandingCredit(from.outstandingCredit ?: 0.0)
                .unpaidOrderIds(from.unpaidOrderIds ?: listOf())
                .build()

    override fun mapTo(to: CreditOrdersModel): CreditOrdersRemoteModel =
        CreditOrdersRemoteModel(
                    to.outstandingCredit,
                    to.unpaidOrderIds
                )
}