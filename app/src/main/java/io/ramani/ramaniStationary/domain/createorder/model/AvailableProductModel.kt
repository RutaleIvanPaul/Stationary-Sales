package io.ramani.ramaniStationary.domain.createorder.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class AvailableProductModel(
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    var quantity: Int = 0,
    val secondaryUnitConversion: Int = 0,
    val secondaryUnitQuantity: Int = 0,
    val secondaryUnits: String = "",
    val units: String = "",
    val supplierProductId: String? = null
) : Parcelable {

    class Builder : IBuilder<AvailableProductModel> {
        private var id: String = ""
        private var productId: String = ""
        private var productName: String = ""
        private var quantity: Int = 0
        private var secondaryUnitConversion: Int = 0
        private var secondaryUnitQuantity: Int = 0
        private var secondaryUnits: String = ""
        private var units: String = ""
        private var supplierProductId: String? = null

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun productId(productId: String): Builder {
            this.productId = productId
            return this
        }

        fun productName(productName: String): Builder {
            this.productName = productName
            return this
        }

        fun quantity(quantity: Int): Builder {
            this.quantity = quantity
            return this
        }

        fun secondaryUnitConversion(secondaryUnitConversion: Int): Builder {
            this.secondaryUnitConversion = secondaryUnitConversion
            return this
        }

        fun secondaryUnitQuantity(secondaryUnitQuantity: Int): Builder {
            this.secondaryUnitQuantity = secondaryUnitQuantity
            return this
        }

        fun secondaryUnits(secondaryUnits: String): Builder {
            this.secondaryUnits = secondaryUnits
            return this
        }

        fun units(units: String): Builder {
            this.units = units
            return this
        }

        fun supplierProductId(supplierProductId: String): Builder {
            this.supplierProductId = supplierProductId
            return this
        }

        override fun build(): AvailableProductModel =
            AvailableProductModel(
                id,
                productId,
                productName,
                quantity,
                secondaryUnitConversion,
                secondaryUnitQuantity,
                secondaryUnits,
                units,
                supplierProductId
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeInt(quantity)
        parcel.writeInt(secondaryUnitConversion)
        parcel.writeInt(secondaryUnitQuantity)
        parcel.writeString(secondaryUnits)
        parcel.writeString(units)
        parcel.writeString(supplierProductId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AvailableProductModel> {
        override fun createFromParcel(parcel: Parcel): AvailableProductModel {
            return AvailableProductModel(parcel)
        }

        override fun newArray(size: Int): Array<AvailableProductModel?> {
            return arrayOfNulls(size)
        }
    }

}