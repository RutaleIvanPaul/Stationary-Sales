package io.ramani.ramaniStationary.domain.createorder.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class SaleModel(
    val id: String = "",
    val activityDate: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
) : Parcelable {

    class Builder : IBuilder<SaleModel> {
        private var id: String = ""
        private var activityDate: String = ""
        private var createdAt: String = ""
        private var updatedAt: String = ""

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun activityDate(activityDate: String): Builder {
            this.activityDate = activityDate
            return this
        }

        fun createdAt(createdAt: String): Builder {
            this.createdAt = createdAt
            return this
        }

        fun updatedAt(updatedAt: String): Builder {
            this.updatedAt = updatedAt
            return this
        }

        override fun build(): SaleModel =
            SaleModel(
                id,
                activityDate,
                createdAt,
                updatedAt
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(activityDate)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SaleModel> {
        override fun createFromParcel(parcel: Parcel): SaleModel {
            return SaleModel(parcel)
        }

        override fun newArray(size: Int): Array<SaleModel?> {
            return arrayOfNulls(size)
        }
    }

}
