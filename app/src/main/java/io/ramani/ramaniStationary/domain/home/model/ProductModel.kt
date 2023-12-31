package io.ramani.ramaniStationary.domain.home.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

@Entity(tableName = "Product")
data class ProductModel(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var archived: Boolean = false,
    var imagePath: String = "",
    var currency: String = "",
    var units: String = "",
    var hasSecondaryUnitConversion: Boolean = false,
    var secondaryUnitConversion: Int = 0,
    var secondaryUnitName: String = "",
    var productCategories: List<ProductCategoryModel> = listOf(),
    var vatCategory: String = "",
    var supplierId: String = "",
    var supplierProductId: String = "",
    var externalId: String = "",
) : Parcelable {

    @Ignore
    var selectedUnit: String = units
        get() = field.ifEmpty { units }

    @Ignore
    var selectedQuantity: Int = 0

    @Ignore
    var selectedPriceCategory: ProductCategoryModel? = ProductCategoryModel()
        get() = if (field?.name.isNullOrEmpty()) productCategories.first() else field

    fun clearParams() {
        selectedQuantity = 0
        selectedUnit = units
        selectedPriceCategory = null
    }

    class Builder : IBuilder<ProductModel> {
        private var id: String = ""
        private var name: String = ""
        private var archived: Boolean = false
        private var imagePath: String = ""
        private var currency: String = ""
        private var units: String = ""
        private var hasSecondaryUnitConversion: Boolean = false
        private var secondaryUnitConversion: Int = 0
        private var secondaryUnitName: String = ""
        private var productCategories: List<ProductCategoryModel> = listOf()
        private var vatCategory: String = ""
        private var supplierId: String = ""
        private var supplierProductId: String = ""
        private var externalId: String = ""

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun archived(archived: Boolean): Builder {
            this.archived = archived
            return this
        }

        fun imagePath(imagePath: String): Builder {
            this.imagePath = imagePath
            return this
        }

        fun currency(currency: String): Builder {
            this.currency = currency
            return this
        }

        fun units(units: String): Builder {
            this.units = units
            return this
        }

        fun hasSecondaryUnitConversion(hasSecondaryUnitConversion: Boolean): Builder {
            this.hasSecondaryUnitConversion = hasSecondaryUnitConversion
            return this
        }

        fun secondaryUnitConversion(secondaryUnitConversion: Int): Builder {
            this.secondaryUnitConversion = secondaryUnitConversion
            return this
        }

        fun secondaryUnitName(secondaryUnitName: String): Builder {
            this.secondaryUnitName = secondaryUnitName
            return this
        }

        fun productCategories(productCategories: List<ProductCategoryModel>): Builder {
            this.productCategories = productCategories
            return this
        }

        fun vatCategory(vatCategory: String): Builder {
            this.vatCategory = vatCategory
            return this
        }

        fun supplierId(supplierId: String): Builder {
            this.supplierId = supplierId
            return this
        }

        fun supplierProductId(supplierProductId: String): Builder {
            this.supplierProductId = supplierProductId
            return this
        }

        fun externalId(externalId: String): Builder {
            this.externalId = externalId
            return this
        }

        override fun build(): ProductModel =
            ProductModel(
                id,
                name,
                archived,
                imagePath,
                currency,
                units,
                hasSecondaryUnitConversion,
                secondaryUnitConversion,
                secondaryUnitName,
                productCategories,
                vatCategory,
                supplierId,
                supplierProductId,
                externalId
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.createTypedArrayList(ProductCategoryModel) ?: listOf(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeByte(if (archived) 1 else 0)
        parcel.writeString(imagePath)
        parcel.writeString(currency)
        parcel.writeString(units)
        parcel.writeByte(if (hasSecondaryUnitConversion) 1 else 0)
        parcel.writeInt(secondaryUnitConversion)
        parcel.writeString(secondaryUnitName)
        parcel.writeTypedList(productCategories)
        parcel.writeString(vatCategory)
        parcel.writeString(supplierId)
        parcel.writeString(supplierProductId)
        parcel.writeString(externalId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductModel> {
        override fun createFromParcel(parcel: Parcel): ProductModel {
            return ProductModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductModel?> {
            return arrayOfNulls(size)
        }
    }

}