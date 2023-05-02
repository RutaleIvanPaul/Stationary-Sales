package io.ramani.ramaniStationary.domain.credit.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

data class CreditOrdersModel(
    val outstandingCredit: Double = 0.0,
    val unpaidOrderIds: List<String> = listOf()
) : Parcelable {

    class Builder : IBuilder<CreditOrdersModel> {
        private var outstandingCredit: Double = 0.0
        private var unpaidOrderIds: List<String> = listOf()

        fun outstandingCredit(outstandingCredit: Double): Builder {
            this.outstandingCredit = outstandingCredit
            return this
        }

        fun unpaidOrderIds(unpaidOrderIds: List<String>): Builder {
            this.unpaidOrderIds = unpaidOrderIds
            return this
        }

        override fun build(): CreditOrdersModel =
            CreditOrdersModel(
                outstandingCredit,
                unpaidOrderIds
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.createStringArrayList() ?: listOf()
    ) {}


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(outstandingCredit)
        parcel.writeStringList(unpaidOrderIds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreditOrdersModel> {
        override fun createFromParcel(parcel: Parcel): CreditOrdersModel {
            return CreditOrdersModel(parcel)
        }

        override fun newArray(size: Int): Array<CreditOrdersModel?> {
            return arrayOfNulls(size)
        }
    }

}