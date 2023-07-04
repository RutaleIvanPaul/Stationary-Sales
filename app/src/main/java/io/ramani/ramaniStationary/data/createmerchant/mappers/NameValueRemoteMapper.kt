package io.ramani.ramaniStationary.data.createmerchant.mappers

import io.ramani.ramaniStationary.data.createmerchant.models.response.NameValueRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.createmerchant.model.NameValueModel

class NameValueRemoteMapper() : ModelMapper<NameValueRemoteModel, NameValueModel> {

    override fun mapFrom(from: NameValueRemoteModel): NameValueModel =
        NameValueModel.Builder()
                .name(from.name ?: "")
                .value(from.value ?: "")
                .build()

    override fun mapTo(to: NameValueModel): NameValueRemoteModel =
        NameValueRemoteModel(
                to.name,
                to.value
            )
}