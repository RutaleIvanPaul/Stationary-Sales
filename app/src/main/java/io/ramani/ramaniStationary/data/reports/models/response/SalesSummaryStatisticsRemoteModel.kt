package io.ramani.ramaniStationary.data.reports.models.response

import com.google.gson.annotations.SerializedName

data class SalesSummaryStatisticsRemoteModel(
    @SerializedName("companyId")
    val companyId: String?,
    @SerializedName("startDate")
    val startDate: String?,
    @SerializedName("endDate")
    val endDate: String?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("totalSalesValue")
    val totalSalesValue: ValuePercentageRemoteModel?,
    @SerializedName("totalSalesCount")
    val totalSalesCount: ValuePercentageRemoteModel?,
    @SerializedName("totalCreditGiven")
    val totalCreditGiven: ValuePercentageRemoteModel?,
    @SerializedName("merchantsVisited")
    val merchantsVisited: ValuePercentageRemoteModel?,
    @SerializedName("newMerchants")
    val newMerchants: ValuePercentageRemoteModel?,
    @SerializedName("averageTransactionValue")
    val averageTransactionValue: ValuePercentageRemoteModel?
)