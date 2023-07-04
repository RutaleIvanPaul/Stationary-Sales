package io.ramani.ramaniStationary.domain.reports.model

import android.os.Parcel
import android.os.Parcelable
import io.ramani.ramaniStationary.domainCore.entities.IBuilder

/**
 * This class will be shared for all models which consist of only name & value
 */
data class ValuePercentageModel(
    val value: Double = 0.0,
    val percentageIncrease: Double = 0.0
) : Parcelable {

    class Builder : IBuilder<ValuePercentageModel> {
        private var value: Double = 0.0
        private var percentageIncrease: Double = 0.0

        fun value(value: Double): Builder {
            this.value = value
            return this
        }

        fun percentageIncrease(percentageIncrease: Double): Builder {
            this.percentageIncrease = percentageIncrease
            return this
        }

        override fun build(): ValuePercentageModel =
            ValuePercentageModel(
                value,
                percentageIncrease
            )
    }

    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(value)
        parcel.writeDouble(percentageIncrease)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ValuePercentageModel> {
        override fun createFromParcel(parcel: Parcel): ValuePercentageModel {
            return ValuePercentageModel(parcel)
        }

        override fun newArray(size: Int): Array<ValuePercentageModel?> {
            return arrayOfNulls(size)
        }
    }

}
