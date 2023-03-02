package io.ramani.ramaniStationary.data.home.models

import com.google.gson.annotations.SerializedName

data class DailySalesStatsRemoteModel(
    @SerializedName("companyName")
    val companyName: String = "",
    @SerializedName("date")
    val date: String = "",
    @SerializedName("totalSales")
    val totalSales: Int = 0,
    @SerializedName("totalOrders")
    val totalOrders: Int = 0,
    @SerializedName("totalCanceledOrders")
    val totalCanceledOrders: Int = 0,
    @SerializedName("totalNumberOfCustomers")
    val totalNumberOfCustomers: Int = 0
)