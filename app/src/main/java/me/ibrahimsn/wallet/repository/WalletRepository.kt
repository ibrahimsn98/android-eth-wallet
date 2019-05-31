package me.ibrahimsn.wallet.repository

import android.arch.lifecycle.LiveData
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.observers.DisposableCompletableObserver
import me.ibrahimsn.wallet.manager.GethAccountManager
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.room.AppDatabase
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
            Wallet(name, address).apply {
                this.id = walletDao.insert(this)
            }
        }
    }

    fun createWallet(name: String): Single<Wallet> {
        return passwordRepository.generatePassword()
                .flatMap { password ->
                    gethAccountManager.createAccount(password)
                            .compose { wallet ->
                                passwordRepository.setPassword(wallet.blockingGet(), password)
                                    .onErrorResumeNext { err ->
                                        deleteWallet(wallet.blockingGet().address, password)
                                                .lift { observer ->
                                                    object : DisposableCompletableObserver() {
                                                        override fun onComplete() {
                                                            observer.onError(err)
                                                        }

                                                        override fun onError(e: Throwable) {
                                                            observer.onError(e)
                                                        }
                                                    }
                                                }
                                    }
                                    .to { wallet }
                            }
                }.doAfterSuccess {
                    walletDao.insert(it.apply {
                        this.name = name
                    })
                }
    }

    fun importPrivateKeyToWallet(name: String, privateKey: String): Single<Wallet> {
        return passwordRepository.generatePassword()
                .flatMap { password ->
                    gethAccountManager.importPrivateKey(privateKey, password)
                            .compose { wallet ->
                                passwordRepository.setPassword(wallet.blockingGet(), password)
                                        .onErrorResumeNext { err ->
                                            deleteWallet(wallet.blockingGet().address, password)
                                                    .lift { observer ->
                                                        object : DisposableCompletableObserver() {
                                                            override fun onComplete() {
                                                                observer.onError(err)
                                                            }

                                                            override fun onError(e: Throwable) {
                                                                observer.onError(e)
                                                            }
                                                        }
                                                    }
                                        }
                                        .to { wallet }
                            }
                }.doAfterSuccess {
                    walletDao.insert(it.apply {
                        this.name = name
                        this.isWallet = true
                    })
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