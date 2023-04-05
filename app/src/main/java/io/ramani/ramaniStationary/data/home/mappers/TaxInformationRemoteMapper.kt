package io.ramani.ramaniStationary.data.home.mappers

import io.ramani.ramaniStationary.data.home.models.response.TaxInformationRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.home.model.TaxInformationModel

class TaxInformationRemoteMapper : ModelMapper<TaxInformationRemoteModel, TaxInformationModel> {
    override fun mapFrom(from: TaxInformationRemoteModel): TaxInformationModel =
        TaxInformationModel.Builder()
            .id(from.id ?: "")
            .name(from.name ?: "")
            .tin(from.tin ?: "")
            .uin(from.uin ?: "")
            .vrn(from.vrn ?: "")
            .gc(from.gc ?: 0)
            .receiptCode(from.receiptCode ?: "")
            .build()

    override fun mapTo(to: TaxInformationModel): TaxInformationRemoteModel =
        TaxInformationRemoteModel(
            to.id,
            to.name,
            to.tin,
            to.uin,
            to.vrn,
            to.gc,
            to.receiptCode
        )
}