package me.ibrahimsn.wallet.repository

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.manager.GethAccountManager
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.room.AppDatabase
import okhttp3.OkHttpClient
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import java.math.BigInteger
import javax.inject.Inject

class WalletRepository @Inject constructor(private var gethAccountManager: GethAccountManager,
                                           private var preferencesRepository: PreferencesRepository,
                                           private var networkRepository: EthereumNetworkRepository,
                                           appDatabase: AppDatabase,
                                           private var httpClient: OkHttpClient) {

    private val walletDao = appDatabase.walletDao()

    fun fetchWallets(): Single<List<Wallet>> {
        return walletDao.getAll()
    }

    fun deleteAll(): Single<Boolean> {
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

    fun setDefaultWallet(wallet: Wallet) {
        preferencesRepository.setCurrentWalletAddress(wallet.address)
    }

    fun getDefaultWallet(): Single<Wallet?> {
        return Single.fromCallable<String> {
            preferencesRepository.getCurrentWalletAddress()
        }.flatMap {
            this.findWallet(it)
        }
    }

    fun balanceInWei(wallet: Wallet): Single<BigInteger> {
        return Single.fromCallable {
            Web3jFactory.build(HttpService(networkRepository.getDefaultNetwork().rpcServerUrl, httpClient, false))
                    .ethGetBalance(wallet.address, DefaultBlockParameterName.LATEST).send().balance
        }.subscribeOn(Schedulers.io())
    }
}