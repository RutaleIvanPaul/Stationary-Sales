package io.ramani.ramaniStationary.domain.reports.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class SalesSummaryStatisticsModel(
    var companyId: String = "",
    var startDate: String = "",
    var endDate: String = "",
    var currency: String = "",
    var totalSalesValue: ValuePercentageModel = ValuePercentageModel(),
    var totalSalesCount: ValuePercentageModel = ValuePercentageModel(),
    var totalCreditGiven: ValuePercentageModel = ValuePercentageModel(),
    var merchantsVisited: ValuePercentageModel = ValuePercentageModel(),
    var newMerchants: ValuePercentageModel = ValuePercentageModel(),
    var averageTransactionValue: ValuePercentageModel = ValuePercentageModel(),
) : Parcelable {

    class Builder : IBuilder<SalesSummaryStatisticsModel> {
        private var companyId: String = ""
        private var startDate: String = ""
        private var endDate: String = ""
        private var currency: String = ""
        private var totalSalesValue: ValuePercentageModel = ValuePercentageModel()
        private var totalSalesCount: ValuePercentageModel = ValuePercentageModel()
        private var totalCreditGiven: ValuePercentageModel = ValuePercentageModel()
        private var merchantsVisited: ValuePercentageModel = ValuePercentageModel()
        private var newMerchants: ValuePercentageModel = ValuePercentageModel()
        private var averageTransactionValue: ValuePercentageModel = ValuePercentageModel()

        fun companyId(companyId: String): Builder {
            this.companyId = companyId
            return this
        }

        fun startDate(startDate: String): Builder {
            this.startDate = startDate
            return this
        }

        fun endDate(endDate: String): Builder {
            this.endDate = endDate
            return this
        }

        fun currency(currency: String): Builder {
            this.currency = currency
            return this
        }

        fun totalSalesValue(totalSalesValue: ValuePercentageModel): Builder {
            this.totalSalesValue = totalSalesValue
            return this
        }

        fun totalSalesCount(totalSalesCount: ValuePercentageModel): Builder {
            this.totalSalesCount = totalSalesCount
            return this
        }

        fun totalCreditGiven(totalCreditGiven: ValuePercentageModel): Builder {
            this.totalCreditGiven = totalCreditGiven
            return this
        }

        fun merchantsVisited(merchantsVisited: ValuePercentageModel): Builder {
            this.merchantsVisited = merchantsVisited
            return this
        }

        fun newMerchants(newMerchants: ValuePercentageModel): Builder {
            this.newMerchants = newMerchants
            return this
        }

        fun averageTransactionValue(averageTransactionValue: ValuePercentageModel): Builder {
            this.averageTransactionValue = averageTransactionValue
            return this
        }

        override fun build(): SalesSummaryStatisticsModel =
            SalesSummaryStatisticsModel(
                companyId,
                startDate,
                endDate,
                currency,
                totalSalesValue,
                totalSalesCount,
                totalCreditGiven,
                merchantsVisited,
                newMerchants,
                averageTransactionValue
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(companyId)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeString(currency)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SalesSummaryStatisticsModel> {
        override fun createFromParcel(parcel: Parcel): SalesSummaryStatisticsModel {
            return SalesSummaryStatisticsModel(parcel)
        }

        override fun newArray(size: Int): Array<SalesSummaryStatisticsModel?> {
            return arrayOfNulls(size)
        }
    }


}