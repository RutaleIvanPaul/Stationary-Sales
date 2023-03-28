package io.ramani.ramaniStationary.app.createorder.presentation

import io.ramani.ramaniStationary.domain.base.SingleLiveEvent
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domainCore.lang.isNotNull

/**
 * This is shared model class for create order feature
 */

class CREATE_ORDER_MODEL {
    companion object {
        var productsToBeOrdered = mutableListOf<ProductModel>()     // Ordered requested product
        var customer: MerchantModel? = null
        var customerTinNumber = ""
        var customerVrnNumber = ""
        var paymentMethod = ""
        var comment = ""

        val onOrderedProductsUpdatedLiveData = SingleLiveEvent<Boolean>()

        fun addOrRemoveProduct(product: ProductModel) {
            if (product.selectedQuantity == 0) {
                productsToBeOrdered.remove(product)
            } else {
                if (!productsToBeOrdered.contains(product))
                    productsToBeOrdered.add(product)
            }
        }

        fun remove(product: ProductModel) {
            product.clearParams()
            productsToBeOrdered.remove(product)

            onOrderedProductsUpdatedLiveData.postValue(true)
        }

        fun clearAll() {
            productsToBeOrdered.forEach {
                remove(it)
            }

            customer = null
            customerTinNumber = ""
            customerVrnNumber = ""
            comment = ""

            onOrderedProductsUpdatedLiveData.postValue(true)
        }

        fun canFinishOrder(): Boolean = productsToBeOrdered.isNotEmpty() && (customer != null)
        fun findProduct(product: ProductModel): ProductModel? {
            val plist = productsToBeOrdered.filter { _p -> _p.id == product.id }
            return if (plist.isNotEmpty()) plist.first() else null
        }

        fun getTotalOrderedPrice(): Int {
            var totalPrice = 0.0

            productsToBeOrdered.forEach {
                if (it.selectedPriceCategory.isNotNull())
                    totalPrice += (it.selectedPriceCategory?.unitPrice ?: 0.0) * it.selectedQuantity
            }

            return totalPrice.toInt()
        }

        fun getTotalVat(): Double {
            var vat = 0.0

            return vat
        }
    }
}