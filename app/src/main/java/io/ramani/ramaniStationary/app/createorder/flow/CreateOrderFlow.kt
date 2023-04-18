package io.ramani.ramaniStationary.app.createorder.flow

interface CreateOrderFlow {
    fun openAddItems()
    fun openCheckout()

    fun openOrderCompleted(saleIdentify: Long)
    fun openPrintSuccessful()
}