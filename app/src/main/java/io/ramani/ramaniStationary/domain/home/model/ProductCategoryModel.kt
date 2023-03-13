package io.ramani.ramaniStationary.domain.home.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class ProductCategoryModel(
    val id: String = "",
    val name: String = "",
    val categoryId: String = "",
    val unitPrice: Double = 0.0
) : Parcelable {

    class Builder : IBuilder<ProductCategoryModel> {
        private var id: String = ""
        private var name: String = ""
        private var categoryId: String = ""
        private var unitPrice: Double = 0.0

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun categoryId(categoryId: String): Builder {
            this.categoryId = categoryId
            return this
        }

        fun unitPrice(unitPrice: Double): Builder {
            this.unitPrice = unitPrice
            return this
        }

        override fun build(): ProductCategoryModel =
            ProductCategoryModel(
                id,
                name,
                categoryId,
                unitPrice
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(categoryId)
        parcel.writeDouble(unitPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductCategoryModel> {
        override fun createFromParcel(parcel: Parcel): ProductCategoryModel {
            return ProductCategoryModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductCategoryModel?> {
            return arrayOfNulls(size)
        }
    }

}