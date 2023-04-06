package io.ramani.ramaniStationary.domain.createmerchant.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

/**
 * This class will be shared for all models which consist of only name & value
 */
data class NameValueModel(
    val name: String = "",
    val value: String = ""
) : Parcelable {

    class Builder : IBuilder<NameValueModel> {
        private var name: String = ""
        private var value: String = ""

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun value(value: String): Builder {
            this.value = value
            return this
        }

        override fun build(): NameValueModel =
            NameValueModel(
                name,
                value
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NameValueModel> {
        override fun createFromParcel(parcel: Parcel): NameValueModel {
            return NameValueModel(parcel)
        }

        override fun newArray(size: Int): Array<NameValueModel?> {
            return arrayOfNulls(size)
        }
    }

}
