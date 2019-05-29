package me.ibrahimsn.wallet.repository

import android.arch.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Single
import me.ibrahimsn.wallet.manager.GethAccountManager
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.room.AppDatabase
import me.ibrahimsn.wallet.room.WalletDao
import javax.inject.Inject

class WalletRepository @Inject constructor(private var gethAccountManager: GethAccountManager,
                                           private var walletDao: WalletDao) {

    fun fetchWallets(): LiveData<List<Wallet>> {
        return walletDao.getAll()
    }

    fun deleteAllWallets(): Single<Boolean> {
        return Single.fromCallable {
            walletDao.deleteAll()
            true
        }
    }

    fun fetchGethWallets(): Single<MutableList<Wallet>> {
        return gethAccountManager.fetchAccounts()
    }

    private fun findWallet(address: String): Single<Wallet?> {
        return walletDao.find(address)
    }

    fun importPublicAddress(name: String, address: String): Single<Wallet> {
        return Single.fromCallable {
            val wallet = Wallet(name, address)
            wallet.id = walletDao.insert(wallet)
            wallet
        }
    }

    fun createWallet(name: String, password: String): Single<Wallet> {
        return gethAccountManager.createAccount(password).doAfterSuccess {
            it.name = name
            walletDao.insert(it)
        }
    }

    fun importKeystoreToWallet(name: String, store: String, password: String, newPassword: String): Single<Wallet> {
        return gethAccountManager.importKeyStore(store, password, newPassword).doAfterSuccess {
            walletDao.insert(it)
        }
    }

    fun exportWallet(wallet: Wallet, password: String, newPassword: String): Single<String> {
        return gethAccountManager.exportAccount(wallet, password, newPassword)
    }

    fun deleteWallet(address: String, password: String): Completable {
        return gethAccountManager.deleteAccount(address, password).doOnComplete {
            walletDao.delete(address)
        }
    }
}