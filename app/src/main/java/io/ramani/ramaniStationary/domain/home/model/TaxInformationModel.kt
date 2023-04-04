package io.ramani.ramaniStationary.domain.home.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class TaxInformationModel(
    var id: String = "",
    var name: String = "",
    var tin: String = "",
    var uin: String = "",
    var vrn: String = "",
    var gc: Long = 0,
    var receiptCode: String = ""
) : Parcelable {

    fun isVRNNotRegistered(): Boolean = vrn == "NOT REGISTERED" || vrn.isEmpty()

    class Builder : IBuilder<TaxInformationModel> {
        private var id: String = ""
        private var name: String = ""
        private var tin: String = ""
        private var uin: String = ""
        private var vrn: String = ""
        private var gc: Long = 0
        private var receiptCode: String = ""

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun tin(tin: String): Builder {
            this.tin = tin
            return this
        }

        fun uin(uin: String): Builder {
            this.uin = uin
            return this
        }

        fun vrn(vrn: String): Builder {
            this.vrn = vrn
            return this
        }

        fun gc(gc: Long): Builder {
            this.gc = gc
            return this
        }

        fun receiptCode(receiptCode: String): Builder {
            this.receiptCode = receiptCode
            return this
        }

        override fun build(): TaxInformationModel =
            TaxInformationModel(
                id,
                name,
                tin,
                uin,
                vrn,
                gc,
                receiptCode
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readString() ?: "",
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(tin)
        parcel.writeString(uin)
        parcel.writeString(vrn)
        parcel.writeLong(gc)
        parcel.writeString(receiptCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaxInformationModel> {
        override fun createFromParcel(parcel: Parcel): TaxInformationModel {
            return TaxInformationModel(parcel)
        }

        override fun newArray(size: Int): Array<TaxInformationModel?> {
            return arrayOfNulls(size)
        }
    }

    fun toJson(): String = Gson().toJson(this)

}