package io.ramani.ramaniStationary.domain.home.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class MerchantModel(
    val id: String = "",
    val name: String = "",
    val isActive: Boolean = false,
    val gps: String = "",
    val salesPersonUID: String = "",
    val salesPersonName: String = "",
    val members: List<MerchantMemberModel> = listOf(),
    val merchantTIN: String = "",
    val city: String = "",
    val creditLimit: Int = 0,
    val merchantType: String = "Default"
) : Parcelable {

    class Builder : IBuilder<MerchantModel> {
        private var id: String = ""
        private var name: String = ""
        private var isActive: Boolean = false
        private var gps: String = ""
        private var salesPersonUID: String = ""
        private var salesPersonName: String = ""
        private var members: List<MerchantMemberModel> = listOf()
        private var merchantTIN: String = ""
        private var city: String = ""
        private var creditLimit: Int = 0
        private var merchantType: String = "Default"

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun isActive(isActive: Boolean): Builder {
            this.isActive = isActive
            return this
        }

        fun gps(gps: String): Builder {
            this.gps = gps
            return this
        }

        fun salesPersonUID(salesPersonUID: String): Builder {
            this.salesPersonUID = salesPersonUID
            return this
        }

        fun salesPersonName(salesPersonName: String): Builder {
            this.salesPersonName = salesPersonName
            return this
        }

        fun members(members: List<MerchantMemberModel>): Builder {
            this.members = members
            return this
        }

        fun merchantTIN(merchantTIN: String): Builder {
            this.merchantTIN = merchantTIN
            return this
        }

        fun city(city: String): Builder {
            this.city = city
            return this
        }

        fun creditLimit(creditLimit: Int): Builder {
            this.creditLimit = creditLimit
            return this
        }

        fun merchantType(merchantType: String): Builder {
            this.merchantType = merchantType
            return this
        }

        override fun build(): MerchantModel =
            MerchantModel(
                id,
                name,
                isActive,
                gps,
                salesPersonUID,
                salesPersonName,
                members,
                merchantTIN,
                city,
                creditLimit,
                merchantType
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(MerchantMemberModel) ?: listOf(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeString(gps)
        parcel.writeString(salesPersonUID)
        parcel.writeString(salesPersonName)
        parcel.writeTypedList(members)
        parcel.writeString(merchantTIN)
        parcel.writeString(city)
        parcel.writeInt(creditLimit)
        parcel.writeString(merchantType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MerchantModel> {
        override fun createFromParcel(parcel: Parcel): MerchantModel {
            return MerchantModel(parcel)
        }

        override fun newArray(size: Int): Array<MerchantModel?> {
            return arrayOfNulls(size)
        }
    }

}