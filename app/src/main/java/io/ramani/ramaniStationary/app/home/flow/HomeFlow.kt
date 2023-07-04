package io.ramani.ramaniStationary.app.home.flow

interface HomeFlow {
    fun openLogin()
    fun openHome()
    fun openStock()
    fun openHistory()
    fun openCredit()

    fun openAllTodaySales()
    fun openAllCustomers()

    fun openCreateNewOrder()
    fun openSalesReports()
    fun openCreateMerchant()

    fun onBackPressed()

    fun openOrderDetails(orderID: String)
}