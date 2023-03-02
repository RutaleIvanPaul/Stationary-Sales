package io.ramani.ramaniStationary.domain.home.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class MerchantMemberModel(
    val name: String = "",
    val phoneNumber: String = "",
) : Parcelable {

    class Builder : IBuilder<MerchantMemberModel> {
        private var name: String = ""
        private var phoneNumber: String = ""

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun phoneNumber(phoneNumber: String): Builder {
            this.phoneNumber = phoneNumber
            return this
        }

        override fun build(): MerchantMemberModel =
            MerchantMemberModel(
                name,
                phoneNumber
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(phoneNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MerchantMemberModel> {
        override fun createFromParcel(parcel: Parcel): MerchantMemberModel {
            return MerchantMemberModel(parcel)
        }

        override fun newArray(size: Int): Array<MerchantMemberModel?> {
            return arrayOfNulls(size)
        }
    }

}