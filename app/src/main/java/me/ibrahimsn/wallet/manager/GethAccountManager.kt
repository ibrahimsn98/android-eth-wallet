package me.ibrahimsn.wallet.manager

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import me.ibrahimsn.wallet.entity.Wallet
import org.ethereum.geth.*
import java.io.File
import java.lang.Exception
import java.math.BigInteger
import java.nio.charset.Charset
import javax.inject.Inject

class GethAccountManager @Inject constructor(keyStoreFile: File) {

    private val keyStore = KeyStore(keyStoreFile.absolutePath, Geth.LightScryptN, Geth.LightScryptP)

    fun createAccount(password: String): Single<Wallet> {
        return Single.fromCallable<Wallet> {
            Wallet(keyStore.newAccount(password).address.hex.toLowerCase())
        }
    }

    fun importKeyStore(store: String, password: String, newPassword: String): Single<Wallet> {
        return Single.fromCallable<Wallet> {
            val account = keyStore.importKey(store.toByteArray(Charset.forName("UTF-8")),
                    password, newPassword)
            Wallet(account.address.hex.toLowerCase())
        }
    }

    fun exportAccount(wallet: Wallet, password: String, newPassword: String): Single<String> {
        return Single.fromCallable<Account> {
            findAccount(wallet.address)
        }.flatMap {
            Single.fromCallable {
                String(keyStore.exportKey(it, password, newPassword))
            }
        }
    }

    fun deleteAccount(address: String, password: String): Completable {
        return Single.fromCallable<Account> {
            findAccount(address)
        }.flatMapCompletable {
            Completable.fromAction {
                keyStore.deleteAccount(it, password)
            }
        }
    }

    fun hasAccount(address: String): Boolean {
        return keyStore.hasAddress(Address(address))
    }

    fun signTransaction(signer: Wallet, signerPassword: String,
                        toAddress: String, amount: BigInteger,
                        gasPrice: BigInteger, gasLimit: Long,
                        nonce: Long, data: ByteArray,
                        chainId: Long): Single<ByteArray> {

        return Single.fromCallable {
            val value = BigInt(0)
            value.setString(amount.toString(), 10)

            val gasPriceBI = BigInt(0)
            gasPriceBI.setString(gasPrice.toString(), 10)

            val tx = Transaction(nonce, Address(toAddress), value, gasLimit, gasPriceBI, data)

            val gethAccount = findAccount(signer.address)
            keyStore.unlock(gethAccount, signerPassword)
            val signed = keyStore.signTx(gethAccount, tx, BigInt(chainId))
            keyStore.lock(gethAccount.address)

            signed.encodeRLP()
        }
    }

    fun fetchAccounts(): Single<MutableList<Wallet>> {
        return Single.fromCallable {
            val accounts = keyStore.accounts
            val result = mutableListOf<Wallet>()

            for (i in 0 until accounts.size())
                result.add(Wallet(accounts.get(i).address.hex.toLowerCase()))

            result
        }
    }

    private fun findAccount(address: String): Account {
        val accounts = keyStore.accounts
        for (i in 0 until accounts.size()) {
            try {
                Log.d("###", "Account find: ${accounts.get(i).address.hex}")
                if (accounts.get(i).address.hex.toString().equals(address, true))
                    return accounts.get(i)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        throw Exception("Wallet with address $address not found!")
    }
}