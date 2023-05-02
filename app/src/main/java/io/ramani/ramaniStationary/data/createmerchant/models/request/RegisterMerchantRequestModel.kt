package io.ramani.ramaniStationary.data.createmerchant.models.request

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.domain.base.v2.Params
import io.ramani.ramaniStationary.domain.credit.model.CreditOrdersModel
import io.ramani.ramaniStationary.domain.home.model.MerchantMemberModel
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class RegisterMerchantRequestModel(
    val name: String,
    val gps: String,
    val sellerId: String,
    val salesPersonUID: String,
    val salesPersonName: String,
    val merchantStatus: Int,
    val categories: Int,
    val merchantType: Int,
    val merchantTIN: String,
    val merchantVRN: String,
    var members: List<MerchantMemberModel> = listOf(),
    val isActive: Boolean = false,
    val routes: List<MerchantRouteModel> = mutableListOf(MerchantRouteModel("WalkIn", "WalkIn")),
    val metaData: List<MetaDataItem> = mutableListOf(MetaDataItem("WalkIn", "WalkIn", "WalkIn")),
): Params

data class MerchantRouteModel(
    val routeId: String,
    val routeName: String
): Params, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(routeId)
        parcel.writeString(routeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MerchantRouteModel> {
        override fun createFromParcel(parcel: Parcel): MerchantRouteModel {
            return MerchantRouteModel(parcel)
        }

        override fun newArray(size: Int): Array<MerchantRouteModel?> {
            return arrayOfNulls(size)
        }
    }

    class Builder : IBuilder<MerchantRouteModel> {
        private var routeId: String = ""
        private var routeName: String = ""

        fun routeId(routeId: String): Builder {
            this.routeId = routeId
            return this
        }

        fun routeName(routeName: String): Builder {
            this.routeName = routeName
            return this
        }

        override fun build(): MerchantRouteModel =
            MerchantRouteModel(
                routeId,
                routeName
            )
    }

}

data class MetaDataItem(
    val id: String,
    val value: String,
    val fieldId: String
): Params, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(value)
        parcel.writeString(fieldId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MetaDataItem> {
        override fun createFromParcel(parcel: Parcel): MetaDataItem {
            return MetaDataItem(parcel)
        }

        override fun newArray(size: Int): Array<MetaDataItem?> {
            return arrayOfNulls(size)
        }
    }

    class Builder : IBuilder<MetaDataItem> {
        private var id: String = ""
        private var value: String = ""
        private var fieldId: String = ""

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun value(value: String): Builder {
            this.value = value
            return this
        }

        fun fieldId(fieldId: String): Builder {
            this.fieldId = fieldId
            return this
        }

        override fun build(): MetaDataItem =
            MetaDataItem(
                id,
                value,
                fieldId
            )
    }
}