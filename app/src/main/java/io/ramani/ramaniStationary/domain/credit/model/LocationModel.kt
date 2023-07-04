package io.ramani.ramaniStationary.domain.credit.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.data.createmerchant.models.request.MerchantRouteModel
import io.ramani.ramaniStationary.data.createmerchant.models.request.MetaDataItem
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class LocationModel(
    val name: String = "",
    val salesPersonName: String = "",
    val salesPersonUuid: String = "",
    val creditOrders: CreditOrdersModel = CreditOrdersModel(),
    val maxCredit: Double = 0.0,
    var memberNumber: String = "",
    val merchantId: String = "",
    val merchantTIN: String = "",
    val merchantVRN: String = "",
    val merchantType: String = "",
    val merchantStatus: String = "",
    val routes: List<MerchantRouteModel> = listOf(),
    val metaData: List<MetaDataItem> = listOf(),
    val gps: String = "",
    val isActive: Boolean = false
) : Parcelable {

    class Builder : IBuilder<LocationModel> {
        private var name: String = ""
        private var salesPersonName: String = ""
        private var salesPersonUuid: String = ""
        private var creditOrders: CreditOrdersModel = CreditOrdersModel()
        private var maxCredit: Double = 0.0
        private var memberNumber: String = ""
        private var merchantId: String = ""
        private var merchantTIN: String = ""
        private var merchantVRN: String = ""
        private var merchantType: String = ""
        private var merchantStatus: String = ""
        private var routes: List<MerchantRouteModel> = listOf()
        private var metaData: List<MetaDataItem> = listOf()
        private var gps: String = ""
        private var isActive: Boolean = false

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun salesPersonName(salesPersonName: String): Builder {
            this.salesPersonName = salesPersonName
            return this
        }

        fun salesPersonUuid(salesPersonUuid: String): Builder {
            this.salesPersonUuid = salesPersonUuid
            return this
        }

        fun creditOrders(creditOrders: CreditOrdersModel): Builder {
            this.creditOrders = creditOrders
            return this
        }

        fun maxCredit(maxCredit: Double): Builder {
            this.maxCredit = maxCredit
            return this
        }

        fun memberNumber(memberNumber: String): Builder {
            this.memberNumber = memberNumber
            return this
        }

        fun merchantId(merchantId: String): Builder {
            this.merchantId = merchantId
            return this
        }

        fun merchantTIN(merchantTIN: String): Builder {
            this.merchantTIN = merchantTIN
            return this
        }

        fun merchantVRN(merchantVRN: String): Builder {
            this.merchantVRN = merchantVRN
            return this
        }

        fun merchantType(merchantType: String): Builder {
            this.merchantType = merchantType
            return this
        }

        fun merchantStatus(merchantStatus: String): Builder {
            this.merchantStatus = merchantStatus
            return this
        }

        fun routes(routes: List<MerchantRouteModel>): Builder {
            this.routes = routes
            return this
        }

        fun metaData(metaData: List<MetaDataItem>): Builder {
            this.metaData = metaData
            return this
        }

        fun gps(gps: String): Builder {
            this.gps = gps
            return this
        }

        fun isActive(isActive: Boolean): Builder {
            this.isActive = isActive
            return this
        }

        override fun build(): LocationModel =
            LocationModel(
                name,
                salesPersonName,
                salesPersonUuid,
                creditOrders,
                maxCredit,
                memberNumber,
                merchantId,
                merchantTIN,
                merchantVRN,
                merchantType,
                merchantStatus,
                routes,
                metaData,
                gps,
                isActive
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(CreditOrdersModel::class.java.classLoader) ?: CreditOrdersModel(),
        parcel.readDouble() ?: 0.0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(MerchantRouteModel) ?: listOf(),
        parcel.createTypedArrayList(MetaDataItem) ?: listOf(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(salesPersonName)
        parcel.writeString(salesPersonUuid)
        parcel.writeParcelable(creditOrders, flags)
        parcel.writeDouble(maxCredit)
        parcel.writeString(memberNumber)
        parcel.writeString(merchantId)
        parcel.writeString(merchantTIN)
        parcel.writeString(merchantVRN)
        parcel.writeString(merchantType)
        parcel.writeString(merchantStatus)
        parcel.writeTypedList(routes)
        parcel.writeTypedList(metaData)
        parcel.writeString(gps)
        parcel.writeByte(if (isActive) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationModel> {
        override fun createFromParcel(parcel: Parcel): LocationModel {
            return LocationModel(parcel)
        }

        override fun newArray(size: Int): Array<LocationModel?> {
            return arrayOfNulls(size)
        }
    }

}