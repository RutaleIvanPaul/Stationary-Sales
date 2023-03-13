package io.ramani.ramaniStationary.data.home.models.response

import com.google.gson.annotations.SerializedName

data class DailySalesStatsRemoteModel(
    @SerializedName("companyName")
    val companyName: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("totalSales")
    val totalSales: Int?,
    @SerializedName("totalOrders")
    val totalOrders: Int?,
    @SerializedName("totalCanceledOrders")
    val totalCanceledOrders: Int?,
    @SerializedName("totalNumberOfCustomers")
    val totalNumberOfCustomers: Int?
)