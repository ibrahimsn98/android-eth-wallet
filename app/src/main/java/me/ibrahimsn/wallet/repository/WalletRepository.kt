package me.ibrahimsn.wallet.repository

import android.arch.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
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

    fun importKeystore(name: String, store: String, backupPassword: String): Completable {
        return Completable.fromSingle(passwordRepository.generatePassword()
                .flatMap { newPassword ->
                    gethAccountManager.importKeyStore(store, backupPassword, newPassword)
                }.flatMap {
                    passwordRepository.setPassword(it.first, it.second).toSingle { it.first }
                }.doOnSuccess {
                    walletDao.insert(it.apply {
                        this.name = name
                    })
                })
    }

    fun exportWallet(wallet: Wallet, backupPassword: String): Single<String> {
        return passwordRepository.getPassword(wallet)
                .flatMap { password ->
                    gethAccountManager.exportAccount(wallet, password, backupPassword)
                }
    }

    fun deleteWallet(wallet: Wallet): Completable {
        return if (wallet.isWallet)
            passwordRepository.getPassword(wallet).flatMapCompletable { password ->
                gethAccountManager.deleteAccount(wallet.address, password)
            }.doOnComplete {
                walletDao.delete(wallet)
            }
        else Completable.fromAction {
            walletDao.delete(wallet)
        }
    }
}