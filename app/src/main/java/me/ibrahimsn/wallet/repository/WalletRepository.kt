package me.ibrahimsn.wallet.repository

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.Wallet
import org.ethereum.geth.KeyStore
import java.io.File

class WalletRepository {

    private lateinit var keyStoreFile: File
    private lateinit var keyStore: KeyStore

    fun createAccount(password: String): Single<Wallet> {
        return Single.fromCallable<Wallet> {
            Wallet(keyStore.newAccount(password).address.hex.toLowerCase())
        }.subscribeOn(Schedulers.io())
    }



}