package me.ibrahimsn.wallet.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.manager.GethAccountManager
import me.ibrahimsn.wallet.entity.Wallet
import okhttp3.OkHttpClient
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import java.math.BigInteger
import javax.inject.Inject

class WalletRepository @Inject constructor(private var gethAccountManager: GethAccountManager,
                       private var preferencesRepository: PreferencesRepository,
                       private var networkRepository: EthereumNetworkRepository,
                       private var httpClient: OkHttpClient) {

    fun fetchWallets(): Single<MutableList<Wallet>> {
        return gethAccountManager.fetchAccounts()
    }

    fun findWallet(address: String): Single<Wallet?> {
        return fetchWallets().flatMap {
            for (wallet in it)
                if (wallet.sameAddress(address))
                    Single.just(wallet)

            null
        }
    }

    fun createWallet(password: String): Single<Wallet> {
        return gethAccountManager.createAccount(password)
    }

    fun importKeystoreToWallet(store: String, password: String, newPassword: String): Single<Wallet> {
        return gethAccountManager.importKeyStore(store, password, newPassword)
    }

    fun exportWallet(wallet: Wallet, password: String, newPassword: String): Single<String> {
        return gethAccountManager.exportAccount(wallet, password, newPassword)
    }

    fun deleteWallet(address: String, password: String): Completable {
        return gethAccountManager.deleteAccount(address, password)
    }

    fun setDefaultWallet(wallet: Wallet): Completable {
        return Completable.fromAction { preferencesRepository.setCurrentWalletAddress(wallet.address) }
    }

    fun getDefaultWallet(): Single<Wallet> {
        return Single.fromCallable<String> {
            preferencesRepository.getCurrentWalletAddress()
        }.flatMap<Wallet> {
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