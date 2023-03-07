package io.ramani.ramaniStationary.domain.auth.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class UserModel(
    val uuid: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val token: String = "",
    val companyId: String = "",
    val currency: String = "",
    val timeZone: String = "",
) : Parcelable {


    class Builder : IBuilder<UserModel> {
        private var uuid: String = ""
        private var name: String = ""
        private var phoneNumber: String = ""
        private var token: String = ""
        private var companyId: String = ""
        private var currency: String = ""
        private var timeZone: String = ""
        fun phoneNumber(phoneNumber: String): Builder {
            this.phoneNumber = phoneNumber
            return this
        }

        fun token(token: String): Builder {
            this.token = token
            return this
        }

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun companyId(companyId: String): Builder {
            this.companyId = companyId
            return this
        }

        fun uuid(uuid: String): Builder {
            this.uuid = uuid
            return this
        }

        fun currency(currency: String): Builder {
            this.currency = currency
            return this
        }

        fun timeZone(timeZone: String): Builder {
            this.timeZone = timeZone
            return this
        }

        override fun build(): UserModel =
            UserModel(
                uuid,
                name,
                phoneNumber,
                token,
                companyId,
                currency,
                timeZone
            )
    }


    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun toString(): String {
        return Gson().toJson(this)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uuid)
        parcel.writeString(name)
        parcel.writeString(phoneNumber)
        parcel.writeString(token)
        parcel.writeString(companyId)
        parcel.writeString(currency)
        parcel.writeString(timeZone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}