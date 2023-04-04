package io.ramani.ramaniStationary.domain.home.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class DailySalesStatsModel(
    val companyName: String = "",
    val date: String = "",
    val totalSales: Double = 0.0,
    val totalOrders: Int = 0,
    val totalCanceledOrders: Int = 0,
    val totalNumberOfCustomers: Int = 0,
) : Parcelable {

    class Builder : IBuilder<DailySalesStatsModel> {
        private var companyName: String = ""
        private var date: String = ""
        private var totalSales: Double = 0.0
        private var totalOrders: Int = 0
        private var totalCanceledOrders: Int = 0
        private var totalNumberOfCustomers: Int = 0

        fun companyName(companyName: String): Builder {
            this.companyName = companyName
            return this
        }

        fun date(date: String): Builder {
            this.date = date
            return this
        }

        fun totalSales(totalSales: Double): Builder {
            this.totalSales = totalSales
            return this
        }

        fun totalOrders(totalOrders: Int): Builder {
            this.totalOrders = totalOrders
            return this
        }

        fun totalCanceledOrders(totalCanceledOrders: Int): Builder {
            this.totalCanceledOrders = totalCanceledOrders
            return this
        }

        fun totalNumberOfCustomers(totalNumberOfCustomers: Int): Builder {
            this.totalNumberOfCustomers = totalNumberOfCustomers
            return this
        }

        override fun build(): DailySalesStatsModel =
            DailySalesStatsModel(
                companyName,
                date,
                totalSales,
                totalOrders,
                totalCanceledOrders,
                totalNumberOfCustomers
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(companyName)
        parcel.writeString(date)
        parcel.writeDouble(totalSales)
        parcel.writeInt(totalOrders)
        parcel.writeInt(totalCanceledOrders)
        parcel.writeInt(totalNumberOfCustomers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DailySalesStatsModel> {
        override fun createFromParcel(parcel: Parcel): DailySalesStatsModel {
            return DailySalesStatsModel(parcel)
        }

        override fun newArray(size: Int): Array<DailySalesStatsModel?> {
            return arrayOfNulls(size)
        }
    }

}