package me.ibrahimsn.wallet.repository

import android.arch.lifecycle.LiveData
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.observers.DisposableCompletableObserver
import me.ibrahimsn.wallet.manager.GethAccountManager
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.room.WalletDao
import javax.inject.Inject

class WalletRepository @Inject constructor(private var gethAccountManager: GethAccountManager,
                                           private var walletDao: WalletDao,
                                           private var passwordRepository: PasswordRepository,
                                           private var preferencesRepository: PreferencesRepository) {

    fun fetchWallets(): LiveData<List<Wallet>> {
        return walletDao.getAll()
    }

    fun getCurrentWallet(): Maybe<Wallet?> {
        return walletDao.getAllRx().flatMapMaybe {
            val currentAddress = preferencesRepository.getCurrentWalletAddress()
            var w : Wallet? = null

            it.forEach { wallet ->
                if (wallet.address == currentAddress)
                    w = wallet
            }

            if (w != null)
                Maybe.just(w)
            else
                Maybe.empty()
        }
    }

    fun findGethWallet(address: String): Single<Boolean> {
        return gethAccountManager.fetchAccounts().flatMap<Boolean> {
            var exists = false

            for (wallet in it)
                if (wallet.address == address)
                    exists = true

            Single.just(exists)
        }
    }

    fun importPublicAddress(name: String, address: String): Completable {
        return Completable.fromCallable {
            Wallet(name, address).apply {
                this.id = walletDao.insert(this)
            }
        }
    }

    fun createWallet(name: String): Single<Wallet> {
        return passwordRepository.generatePassword().flatMap { password ->
            gethAccountManager.createAccount(password)
        }.flatMap {
            passwordRepository.setPassword(it.first, it.second).toSingle { it.first }
        }.doOnSuccess {
            walletDao.insert(it.apply {
                this.name = name
                this.isWallet = true
            })
        }
    }

    fun importPrivateKeyToWallet(name: String, privateKey: String): Completable {
        return Completable.fromSingle(passwordRepository.generatePassword().flatMap { password ->
            gethAccountManager.importPrivateKey(privateKey, password)
        }.flatMap {
            passwordRepository.setPassword(it.first, it.second).toSingle { it.first }
        }.doAfterSuccess {
            walletDao.insert(it.apply {
                this.name = name
                this.isWallet = true
            })
        })
    }

    fun updateWalletName(wallet: Wallet, newName: String): Completable {
        return Completable.fromSingle(walletDao.find(wallet.address).flatMap {
            Single.just(it.apply {
                walletDao.update(wallet.apply {
                    this.name = newName
                })
            })
        })
    }

    /*fun importKeystoreToWallet(name: String, store: String, password: String, newPassword: String): Single<Wallet> {
        return gethAccountManager.importKeyStore(store, password, newPassword).doAfterSuccess {
            walletDao.insert(it)
        }
    }*/

    fun exportWallet(wallet: Wallet, backupPassword: String): Single<String> {
        return passwordRepository.getPassword(wallet)
                .flatMap { password ->
                    gethAccountManager.exportAccount(wallet, password, backupPassword)
                }
    }

    private fun deleteWallet(wallet: Wallet, password: String): Completable {
        return Completable.fromAction { gethAccountManager.deleteAccount(wallet.address, password) }
    }

    fun deleteWallet(wallet: Wallet): Completable {
        return passwordRepository.getPassword(wallet).flatMapCompletable { password ->
            gethAccountManager.deleteAccount(wallet.address, password)
        }.doOnComplete {
            walletDao.delete(wallet)
        }
    }
}