package io.ramani.ramaniStationary.data.home.mappers

import io.ramani.ramaniStationary.data.home.models.response.TaxRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.home.model.TaxModel

class TaxRemoteMapper : ModelMapper<TaxRemoteModel, TaxModel> {
    override fun mapFrom(from: TaxRemoteModel): TaxModel =
        TaxModel.Builder()
            .id(from.id)
            .name(from.name)
            .tin(from.tin)
            .uin(from.uin)
            .vrn(from.vrn)
            .updatedAt(from.updatedAt)
            .build()

    override fun mapTo(to: TaxModel): TaxRemoteModel =
        TaxRemoteModel(
            to.id,
            to.name,
            to.tin,
            to.uin,
            to.vrn,
            to.updatedAt
        )
}