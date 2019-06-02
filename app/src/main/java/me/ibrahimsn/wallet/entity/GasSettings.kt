package me.ibrahimsn.wallet.entity

import android.os.Parcel
import android.os.Parcelable
import java.math.BigInteger

class GasSettings : Parcelable {

    var gasPrice: BigInteger
    var gasLimit: Long

    constructor(gasPrice: BigInteger, gasLimit: Long) {
        this.gasPrice = gasPrice
        this.gasLimit = gasLimit
    }

    constructor(source: Parcel) {
        gasPrice = BigInteger(source.readString())
        gasLimit = source.readString()!!.toLong()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(gasPrice.toString(10))
        dest.writeString(gasLimit.toString(10))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GasSettings> {
        override fun createFromParcel(source: Parcel): GasSettings {
            return GasSettings(source)
        }

        override fun newArray(size: Int): Array<GasSettings?> {
            return arrayOfNulls(size)
        }
    }
}