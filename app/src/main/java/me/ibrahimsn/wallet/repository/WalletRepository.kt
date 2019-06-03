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

    /**
     * Fetch all wallets from database and compare each with
     * current wallet address stored in sharedPreferences.
     */
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

    /**
     * If doesn't exists, insert wallet with "public address" into database
     */
    fun importPublicAddress(name: String, address: String): Single<Wallet> {
        return Single.fromCallable {
            if (walletDao.count(address) > 0)
                throw Exception("This address is already imported!")
            else
                Wallet(name, address).apply {
                    walletDao.insert(this)
                }
        }
    }

    /**
     * Generate password for new wallet
     * Save password into keystore
     * If succeed, insert wallet into database
     */
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

    /**
     * Generate password for new wallet
     * Import private key into GethAccountManager
     * If imported wallet's public address exists in keystore, delete it back from GethAccountManager
     * Save generated password into keystore
     * If succeed, insert wallet into database
     */
    fun importPrivateKeyToWallet(name: String, privateKey: String): Single<Wallet> {
        return passwordRepository.generatePassword().flatMap { password ->
            gethAccountManager.importPrivateKey(privateKey, password)
        }.flatMap { pair ->
            if (passwordRepository.isExists(pair.first.address)) {
                gethAccountManager.deleteAccount(pair.first.address, pair.second)
                throw Exception("This address has already exists in keystore.")
            }

            passwordRepository.setPassword(pair.first, pair.second).toSingle { pair.first }
        }.doAfterSuccess {
            walletDao.insert(it.apply {
                this.name = name
                this.isWallet = true
            })
        }
    }

    /**
     * Generate password for new wallet
     * Import keystore into GethAccountManager
     * If imported wallet's public address exists in Android keystore, delete it back from GethAccountManager
     * Save generated password into keystore
     * If succeed, insert wallet into database
     *
     * @param backupPassword - password that identified while exporting keystore
     */
    fun importKeystore(name: String, store: String, backupPassword: String): Single<Wallet> {
        return passwordRepository.generatePassword()
                .flatMap { newPassword ->
                    gethAccountManager.importKeystore(store, backupPassword, newPassword)
                }.flatMap { pair ->
                    if (passwordRepository.isExists(pair.first.address)) {
                        gethAccountManager.deleteAccount(pair.first.address, pair.second)
                        throw Exception("This address has already exists in keystore.")
                    }

                    passwordRepository.setPassword(pair.first, pair.second).toSingle { pair.first }
                }.doOnSuccess {
                    walletDao.insert(it.apply {
                        this.name = name
                        this.isWallet = true
                    })
                }
    }

    /**
     * Export wallet keystore string
     * @param backupPassword - password that used to import keystore back
     */
    fun exportWallet(wallet: Wallet, backupPassword: String): Single<String> {
        return passwordRepository.getPassword(wallet)
                .flatMap { password ->
                    gethAccountManager.exportAccount(wallet, password, backupPassword)
                }
    }

    fun fetchWallets(): LiveData<List<Wallet>> {
        return walletDao.getAll()
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