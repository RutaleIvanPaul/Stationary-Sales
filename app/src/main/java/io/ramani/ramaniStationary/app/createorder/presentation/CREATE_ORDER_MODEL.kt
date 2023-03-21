package io.ramani.ramaniStationary.app.createorder.presentation

import io.ramani.ramaniStationary.domain.home.model.ProductModel

/**
 * This is shared model class for create order feature
 */

class CREATE_ORDER_MODEL {
    companion object {
        var productsToBeOrdered = HashMap<ProductModel, Pair<String, Int>>()     // Ordered requested product

    }
}