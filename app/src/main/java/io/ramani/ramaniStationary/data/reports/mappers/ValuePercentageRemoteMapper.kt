package io.ramani.ramaniStationary.data.reports.mappers

import io.ramani.ramaniStationary.data.reports.models.response.ValuePercentageRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.reports.model.ValuePercentageModel

class ValuePercentageRemoteMapper() : ModelMapper<ValuePercentageRemoteModel, ValuePercentageModel> {

    override fun mapFrom(from: ValuePercentageRemoteModel): ValuePercentageModel =
        ValuePercentageModel.Builder()
                .value(from.value ?: 0.0)
                .percentageIncrease(from.percentageIncrease ?: 0.0)
                .build()

    override fun mapTo(to: ValuePercentageModel): ValuePercentageRemoteModel =
        ValuePercentageRemoteModel(
                to.value,
                to.percentageIncrease
            )
}