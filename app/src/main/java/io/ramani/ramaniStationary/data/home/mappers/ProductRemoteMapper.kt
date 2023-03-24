package io.ramani.ramaniStationary.data.home.mappers

import io.ramani.ramaniStationary.data.home.models.response.ProductCategoryRemoteModel
import io.ramani.ramaniStationary.data.home.models.response.ProductRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.home.model.ProductCategoryModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel

class ProductRemoteMapper(
    private val productCategoryMapper: ModelMapper<ProductCategoryRemoteModel, ProductCategoryModel>,
) : ModelMapper<ProductRemoteModel, ProductModel> {

    override fun mapFrom(from: ProductRemoteModel): ProductModel {
        val productsCategories: ArrayList<ProductCategoryModel> = ArrayList()
        for (eachProduct in from.productCategories ?: listOf()) {
            productsCategories.add(productCategoryMapper.mapFrom(eachProduct))
        }

        return ProductModel.Builder()
                .id(from.id ?: "")
                .name(from.name ?: "")
                .archived(from.archived ?: false)
                .imagePath(from.imagePath ?: "")
                .currency(from.currency ?: "")
                .units(from.units ?: "")
                .hasSecondaryUnitConversion(from.hasSecondaryUnitConversion ?: false)
                .secondaryUnitConversion(from.secondaryUnitConversion ?: 0)
                .secondaryUnitName(from.secondaryUnitName ?: "")
                .productCategories(productsCategories)
                .vatCategory(from.vatCategory ?: "")
                .supplierId(from.supplierId ?: "")
                .supplierProductId(from.supplierProductId ?: "")
                .externalId(from.externalId ?: "")
                .build()
    }

    override fun mapTo(to: ProductModel): ProductRemoteModel {
        val productsCategories: ArrayList<ProductCategoryRemoteModel> = ArrayList()
        for (eachProduct in to.productCategories) {
            productsCategories.add(productCategoryMapper.mapTo(eachProduct))
        }

        return ProductRemoteModel(
                to.id,
                to.name,
                to.archived,
                to.imagePath,
                to.currency,
                to.units,
                to.hasSecondaryUnitConversion,
                to.secondaryUnitConversion,
                to.secondaryUnitName,
                to.vatCategory,
                to.supplierId,
                to.supplierProductId,
                to.externalId,
                productsCategories
            )
    }
}