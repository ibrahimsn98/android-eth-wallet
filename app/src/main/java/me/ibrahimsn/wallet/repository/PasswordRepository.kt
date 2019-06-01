package me.ibrahimsn.wallet.repository

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Single
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.manager.CipherManager
import java.security.SecureRandom
import javax.inject.Inject

class PasswordRepository @Inject constructor(context: Context) {

    private val cipher = CipherManager(context)

    fun getPassword(wallet: Wallet): Single<String> {
        return Single.fromCallable { String(cipher.get(wallet.address)) }
    }

    fun setPassword(wallet: Wallet, password: String): Completable {
        return Completable.fromAction { cipher.put(wallet.address, password) }
    }

    fun generatePassword(): Single<String> {
        return Single.fromCallable {
            val bytes = ByteArray(256)
            val random = SecureRandom()
            random.nextBytes(bytes)
            String(bytes)
        }
    }
}