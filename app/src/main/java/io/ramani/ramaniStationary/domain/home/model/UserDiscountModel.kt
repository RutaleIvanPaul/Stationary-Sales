package io.ramani.ramaniStationary.domain.home.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class UserDiscountModel(
    val name: String = "",
    val companyId: String = "",
    val amount: Double = 0.0,
    val dateCreated: String = ""
) : Parcelable {


    class Builder : IBuilder<UserDiscountModel> {
        private var name: String = ""
        private var companyId: String = ""
        private var amount: Double = 0.0
        private var dateCreated: String = ""

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun companyId(companyId: String): Builder {
            this.companyId = companyId
            return this
        }

        fun amount(amount: Double): Builder {
            this.amount = amount
            return this
        }

        fun dateCreated(dateCreated: String): Builder {
            this.dateCreated = dateCreated
            return this
        }

        override fun build(): UserDiscountModel =
            UserDiscountModel(
                name,
                companyId,
                amount,
                dateCreated
            )
    }


    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    override fun toString(): String {
        return Gson().toJson(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(companyId)
        parcel.writeDouble(amount)
        parcel.writeString(dateCreated)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserDiscountModel> {
        override fun createFromParcel(parcel: Parcel): UserDiscountModel {
            return UserDiscountModel(parcel)
        }

        override fun newArray(size: Int): Array<UserDiscountModel?> {
            return arrayOfNulls(size)
        }
    }
}