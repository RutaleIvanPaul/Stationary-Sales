package io.ramani.ramaniStationary.data.createorder.mappers

import io.ramani.ramaniStationary.data.createorder.models.response.AvailableProductRemoteModel
import io.ramani.ramaniStationary.data.createorder.models.response.AvailableStockRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.createorder.model.AvailableProductModel
import io.ramani.ramaniStationary.domain.createorder.model.AvailableStockModel

class AvailableStockRemoteMapper(
    private val availableProductRemoteMapper: ModelMapper<AvailableProductRemoteModel, AvailableProductModel>,
) : ModelMapper<AvailableStockRemoteModel, AvailableStockModel> {

    override fun mapFrom(from: AvailableStockRemoteModel): AvailableStockModel {
        val products: ArrayList<AvailableProductModel> = ArrayList()
        for (eachProduct in from.products ?: listOf()) {
            products.add(availableProductRemoteMapper.mapFrom(eachProduct))
        }

        return AvailableStockModel.Builder()
                .id(from.id ?: "")
                .companyId(from.companyId ?: "")
                .lastUpdated(from.lastUpdated ?: "")
                .products(products)
                .salesPersonUID(from.salesPersonUID ?: "")
                .v(from.v ?: 0)
                .warehouseId(from.warehouseId ?: "")
                .build()
    }

    override fun mapTo(to: AvailableStockModel): AvailableStockRemoteModel {
        val products: ArrayList<AvailableProductRemoteModel> = ArrayList()
        for (eachProduct in to.products) {
            products.add(availableProductRemoteMapper.mapTo(eachProduct))
        }

        return AvailableStockRemoteModel(
                to.id,
                to.companyId,
                to.lastUpdated,
                products,
                to.salesPersonUID,
                to.v,
                to.warehouseId
            )
    }
}