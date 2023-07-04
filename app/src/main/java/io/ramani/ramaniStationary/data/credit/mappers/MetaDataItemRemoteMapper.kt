package io.ramani.ramaniStationary.data.credit.mappers

import io.ramani.ramaniStationary.data.createmerchant.models.request.MetaDataItem
import io.ramani.ramaniStationary.data.credit.models.response.MetaDataItemRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper

class MetaDataItemRemoteMapper() : ModelMapper<MetaDataItemRemoteModel, MetaDataItem> {
    override fun mapFrom(from: MetaDataItemRemoteModel): MetaDataItem =
        MetaDataItem.Builder()
                .id(from.id ?: "")
                .value(from.value ?: "")
                .fieldId(from.fieldId ?: "")
                .build()

    override fun mapTo(to: MetaDataItem): MetaDataItemRemoteModel =
        MetaDataItemRemoteModel(
                    to.id,
                    to.value,
                    to.fieldId
                )
}