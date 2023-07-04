package io.ramani.ramaniStationary.domain.createorder.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class AvailableStockModel(
    val id: String = "",
    val companyId: String = "",
    val lastUpdated: String = "",
    val products: List<AvailableProductModel> = listOf(),
    val salesPersonUID: String = "",
    val v: Int = 0,
    val warehouseId: String = ""
) : Parcelable {

    class Builder : IBuilder<AvailableStockModel> {
        private var id: String = ""
        private var companyId: String = ""
        private var lastUpdated: String = ""
        private var products: List<AvailableProductModel> = listOf()
        private var salesPersonUID: String = ""
        private var v: Int = 0
        private var warehouseId: String = ""

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun companyId(companyId: String): Builder {
            this.companyId = companyId
            return this
        }

        fun v(v: Int): Builder {
            this.v = v
            return this
        }

        fun lastUpdated(lastUpdated: String): Builder {
            this.lastUpdated = lastUpdated
            return this
        }

        fun products(products: List<AvailableProductModel>): Builder {
            this.products = products
            return this
        }

        fun salesPersonUID(salesPersonUID: String): Builder {
            this.salesPersonUID = salesPersonUID
            return this
        }

        fun warehouseId(warehouseId: String): Builder {
            this.warehouseId = warehouseId
            return this
        }

        override fun build(): AvailableStockModel =
            AvailableStockModel(
                id,
                companyId,
                lastUpdated,
                products,
                salesPersonUID,
                v,
                warehouseId
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(AvailableProductModel) ?: listOf(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(companyId)
        parcel.writeString(lastUpdated)
        parcel.writeTypedList(products)
        parcel.writeString(salesPersonUID)
        parcel.writeInt(v)
        parcel.writeString(warehouseId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AvailableStockModel> {
        override fun createFromParcel(parcel: Parcel): AvailableStockModel {
            return AvailableStockModel(parcel)
        }

        override fun newArray(size: Int): Array<AvailableStockModel?> {
            return arrayOfNulls(size)
        }
    }

}
