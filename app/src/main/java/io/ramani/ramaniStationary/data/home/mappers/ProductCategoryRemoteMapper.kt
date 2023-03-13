package io.ramani.ramaniStationary.data.home.mappers

import io.ramani.ramaniStationary.data.home.models.response.ProductCategoryRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.home.model.ProductCategoryModel

class ProductCategoryRemoteMapper : ModelMapper<ProductCategoryRemoteModel, ProductCategoryModel> {
    override fun mapFrom(from: ProductCategoryRemoteModel): ProductCategoryModel =
        ProductCategoryModel.Builder()
            .id(from.id ?: "")
            .name(from.name ?: "")
            .categoryId(from.categoryId ?: "")
            .unitPrice(from.unitPrice ?: 0.0)
            .build()

    override fun mapTo(to: ProductCategoryModel): ProductCategoryRemoteModel =
        ProductCategoryRemoteModel(
            to.id,
            to.name,
            to.categoryId,
            to.unitPrice
        )
}