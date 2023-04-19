package io.ramani.ramaniStationary.domain.createmerchant.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class TopPerformersModel(
    val topSalesPeople: List<NameValueModel> = listOf(),
    val topMerchants: List<NameValueModel> = listOf(),
    val topProducts: List<NameValueModel> = listOf()
) : Parcelable {

    class Builder : IBuilder<TopPerformersModel> {
        private var topSalesPeople: List<NameValueModel> = listOf()
        private var topMerchants: List<NameValueModel> = listOf()
        private var topProducts: List<NameValueModel> = listOf()

        fun topSalesPeople(topSalesPeople: List<NameValueModel>): Builder {
            this.topSalesPeople = topSalesPeople
            return this
        }

        fun topMerchants(topMerchants: List<NameValueModel>): Builder {
            this.topMerchants = topMerchants
            return this
        }

        fun topProducts(topProducts: List<NameValueModel>): Builder {
            this.topProducts = topProducts
            return this
        }

        override fun build(): TopPerformersModel =
            TopPerformersModel(
                topSalesPeople,
                topMerchants,
                topProducts
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(NameValueModel) ?: listOf(),
        parcel.createTypedArrayList(NameValueModel) ?: listOf(),
        parcel.createTypedArrayList(NameValueModel) ?: listOf(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(topSalesPeople)
        parcel.writeTypedList(topMerchants)
        parcel.writeTypedList(topProducts)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopPerformersModel> {
        override fun createFromParcel(parcel: Parcel): TopPerformersModel {
            return TopPerformersModel(parcel)
        }

        override fun newArray(size: Int): Array<TopPerformersModel?> {
            return arrayOfNulls(size)
        }
    }

}
