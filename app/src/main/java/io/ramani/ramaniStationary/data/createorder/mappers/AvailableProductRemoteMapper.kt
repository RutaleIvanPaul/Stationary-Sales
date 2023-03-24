package io.ramani.ramaniStationary.data.createorder.mappers

import io.ramani.ramaniStationary.data.createorder.models.response.AvailableProductRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.createorder.model.AvailableProductModel

class AvailableProductRemoteMapper() : ModelMapper<AvailableProductRemoteModel, AvailableProductModel> {
    override fun mapFrom(from: AvailableProductRemoteModel): AvailableProductModel =
        AvailableProductModel.Builder()
                .id(from.id ?: "")
                .productId(from.productId ?: "")
                .productName(from.productName ?: "")
                .quantity(from.quantity ?: 0)
                .secondaryUnitConversion(from.secondaryUnitConversion ?: 0)
                .secondaryUnitQuantity(from.secondaryUnitQuantity ?: 0)
                .secondaryUnits(from.secondaryUnits ?: "")
                .units(from.units ?: "")
                .supplierProductId(from.supplierProductId ?: "")
                .build()

    override fun mapTo(to: AvailableProductModel): AvailableProductRemoteModel =
        AvailableProductRemoteModel(
                    to.id,
                    to.productId,
                    to.productName,
                    to.quantity,
                    to.secondaryUnitConversion,
                    to.secondaryUnitQuantity,
                    to.secondaryUnits,
                    to.units,
                    to.supplierProductId
                )
}