package me.ibrahimsn.wallet.repository

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Single
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.util.KS
import java.security.SecureRandom
import javax.inject.Inject

class PasswordRepository @Inject constructor(private val context: Context) {

    fun getPassword(wallet: Wallet): Single<String> {
        return Single.fromCallable { String(KS.get(context, wallet.address)) }
    }

    fun setPassword(wallet: Wallet, password: String): Completable {
        return Completable.fromAction { KS.put(context, wallet.address, password) }
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