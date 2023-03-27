package io.ramani.ramaniStationary.app.createorder.presentation

import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domainCore.lang.isNotNull

/**
 * This is shared model class for create order feature
 */

class CREATE_ORDER_MODEL {
    companion object {
        var productsToBeOrdered = mutableListOf<ProductModel>()     // Ordered requested product

        fun addOrRemoveProduct(product: ProductModel) {
            if (product.selectedQuantity == 0) {
                productsToBeOrdered.remove(product)
            } else {
                if (!productsToBeOrdered.contains(product))
                    productsToBeOrdered.add(product)
            }
        }

        fun getTotalOrderedPrice(): Int {
            var totalPrice = 0.0

            productsToBeOrdered.forEach {
                if (it.productCategories.isNotNull() && it.productCategories.isNotEmpty())
                    totalPrice += it.productCategories[0].unitPrice * it.selectedQuantity
            }

            return totalPrice.toInt()
        }

    }
}