package io.ramani.ramaniStationary.domain.home.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class UserAccountDetailsModel(
    val name: String = "",
    val accountType: String = "",
    val restrictSalesByStockAssigned: Boolean = false,
    val discounts: List<UserDiscountModel> = listOf()
) : Parcelable {

    class Builder : IBuilder<UserAccountDetailsModel> {
        private var name: String = ""
        private var accountType: String = ""
        private var restrictSalesByStockAssigned: Boolean = false
        private var discounts: List<UserDiscountModel> = listOf()

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun accountType(accountType: String): Builder {
            this.accountType = accountType
            return this
        }

        fun restrictSalesByStockAssigned(restrictSalesByStockAssigned: Boolean): Builder {
            this.restrictSalesByStockAssigned = restrictSalesByStockAssigned
            return this
        }

        fun discounts(discounts: List<UserDiscountModel>): Builder {
            this.discounts = discounts
            return this
        }

        override fun build(): UserAccountDetailsModel =
            UserAccountDetailsModel(
                name,
                accountType,
                restrictSalesByStockAssigned,
                discounts
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.createTypedArrayList(UserDiscountModel) ?: listOf()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(accountType)
        parcel.writeByte(if (restrictSalesByStockAssigned) 1 else 0)
        parcel.writeTypedList(discounts)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserAccountDetailsModel> {
        override fun createFromParcel(parcel: Parcel): UserAccountDetailsModel {
            return UserAccountDetailsModel(parcel)
        }

        override fun newArray(size: Int): Array<UserAccountDetailsModel?> {
            return arrayOfNulls(size)
        }
    }

    fun toJson(): String = Gson().toJson(this)

}