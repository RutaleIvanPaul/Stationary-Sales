package io.ramani.ramaniStationary.app.createorder.presentation

import io.ramani.ramaniStationary.data.createorder.models.request.SaleOrderItemModel
import io.ramani.ramaniStationary.data.createorder.models.request.SaleOrderModel
import io.ramani.ramaniStationary.data.createorder.models.request.SaleRequestModel
import io.ramani.ramaniStationary.domain.base.SingleLiveEvent
import io.ramani.ramaniStationary.domain.home.model.MerchantModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel
import io.ramani.ramaniStationary.domain.home.model.TaxInformationModel
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

        fun clear() {
            productsToBeOrdered.forEach {
                it.clearParams()
            }

            productsToBeOrdered.clear()

            customer = null
            customerTinNumber = ""
            customerVrnNumber = ""
            comment = ""

            onOrderedProductsUpdatedLiveData.postValue(true)
        }

        fun canFinishOrder(): Boolean = productsToBeOrdered.isNotEmpty() // && (customer != null)
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

        fun getTotalVat(taxInformation: TaxInformationModel): Int {
            var vat = 0.0

            productsToBeOrdered.forEach {
                val price = it.selectedPriceCategory?.unitPrice ?: 0.0
                val item_cost = it.selectedQuantity * price

                if (it.vatCategory != "E") {
                    if (!taxInformation.isVRNNotRegistered()) {
                        //User's VRN is registered
                        vat += 0.18 * item_cost / 1.18
                    }
                }
            }

            return vat.toInt()
        }

        fun createSaleRequestModel(timeSeconds: Long, companyId: String, companyName: String, userId: String, userName: String, fullTimeStamp: String, checkTime: String, deliveryDate: String, receiptCode: String, vrn: String): SaleRequestModel {
            val orderItems = mutableListOf<SaleOrderItemModel>()
            productsToBeOrdered.forEach {
                orderItems.add(SaleOrderItemModel(
                    it.id,
                    it.name,
                    it.selectedPriceCategory?.unitPrice ?: 0.0 ,
                    it.selectedQuantity.toDouble(),
                    it.vatCategory,
                    it.selectedUnit,
                    it.supplierProductId,
                    it.selectedPriceCategory?.id ?: "",
                    it.selectedPriceCategory?.name ?: ""
                ))
            }

            val totalPrice = getTotalOrderedPrice()

            val order = SaleOrderModel(
                userId,
                customer?.id ?: "",
                companyId,
                3.0,
                deliveryDate,
                deliveryDate,
                comment,
                totalPrice.toDouble(),
                2.0,
                if (paymentMethod == "Paid") 0.0 else 1.0,
                orderItems
            )

            val request = SaleRequestModel(
                companyId,
                companyName,
                customer?.id ?: "",
                customer?.name ?: "walkIn Customer",
                userName,
                userId,
                fullTimeStamp,
                checkTime,
                checkTime,
                customerTinNumber,
                customerVrnNumber,
                listOf(order),
                "",
                totalCost = totalPrice,
                hasNewMerchantTIN = customer != null,
                hasNewMerchantVRN = customer != null,
                printStatus = "Not Printed",
                wasCreatedOrderSelected = true,
                RECEIPTCODE = receiptCode,
                VRN = vrn
            )

            return request
        }
    }
}