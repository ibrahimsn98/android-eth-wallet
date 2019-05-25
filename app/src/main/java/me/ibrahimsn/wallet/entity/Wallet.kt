package me.ibrahimsn.wallet.entity

import android.os.Parcel
import android.os.Parcelable

class Wallet : Parcelable {

    var address: String

    constructor(address: String) {
        this.address = address
    }

    constructor(source: Parcel) {
        this.address = source.readString()!!
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    public fun sameAddress(address: String): Boolean {
        return this.address == address
    }

    companion object CREATOR : Parcelable.Creator<Wallet> {
        override fun createFromParcel(source: Parcel): Wallet {
            return Wallet(source)
        }

        override fun newArray(size: Int): Array<Wallet?> {
            return arrayOfNulls(size)
        }
    }
}