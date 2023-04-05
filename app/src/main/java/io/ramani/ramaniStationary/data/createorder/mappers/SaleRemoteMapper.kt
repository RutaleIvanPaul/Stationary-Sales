package io.ramani.ramaniStationary.data.createorder.mappers

import io.ramani.ramaniStationary.data.createorder.models.response.SaleRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.createorder.model.SaleModel

class SaleRemoteMapper: ModelMapper<SaleRemoteModel, SaleModel> {

    override fun mapFrom(from: SaleRemoteModel): SaleModel =
        SaleModel.Builder()
                .id(from.id ?: "")
                .activityDate(from.activityDate ?: "")
                .createdAt(from.createdAt ?: "")
                .updatedAt(from.updatedAt ?: "")
                .build()

    override fun mapTo(to: SaleModel): SaleRemoteModel =
        SaleRemoteModel(
                to.id,
                to.activityDate,
                to.createdAt,
                to.updatedAt
            )
}