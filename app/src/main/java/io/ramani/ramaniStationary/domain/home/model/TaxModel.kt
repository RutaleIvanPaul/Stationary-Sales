package io.ramani.ramaniStationary.domain.home.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

@Entity(tableName = "Tax")
data class TaxModel(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var tin: String = "",
    var uin: String = "",
    var vrn: String = "",
    var updatedAt: String = "",
) : Parcelable {

    class Builder : IBuilder<TaxModel> {
        private var id: String = ""
        private var name: String = ""
        private var tin: String = ""
        private var uin: String = ""
        private var vrn: String = ""
        private var updatedAt: String = ""

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

        fun updatedAt(updatedAt: String): Builder {
            this.updatedAt = updatedAt
            return this
        }

        override fun build(): TaxModel =
            TaxModel(
                id,
                name,
                tin,
                uin,
                vrn,
                updatedAt
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(tin)
        parcel.writeString(uin)
        parcel.writeString(vrn)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaxModel> {
        override fun createFromParcel(parcel: Parcel): TaxModel {
            return TaxModel(parcel)
        }

        override fun newArray(size: Int): Array<TaxModel?> {
            return arrayOfNulls(size)
        }
    }

}