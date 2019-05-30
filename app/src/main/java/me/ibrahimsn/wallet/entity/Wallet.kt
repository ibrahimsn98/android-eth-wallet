package me.ibrahimsn.wallet.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "wallets")
class Wallet {

    @PrimaryKey var id: Long?
    var name: String?
    var address: String
    var isWallet: Boolean

    constructor(id: Long, name: String, address: String, isWallet: Boolean) {
        this.id = id
        this.name = name
        this.address = address
        this.isWallet = isWallet
    }

    @Ignore
    constructor(address: String) {
        this.id = null
        this.name = null
        this.address = address
        this.isWallet = false
    }

    @Ignore
    constructor(name: String, address: String) {
        this.id = null
        this.name = name
        this.address = address
        this.isWallet = false
    }
}