package me.ibrahimsn.wallet.ui.wallet

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.EtherPriceResponse
import me.ibrahimsn.wallet.entity.EtherScanResponse
import me.ibrahimsn.wallet.entity.Transaction
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.EtherScanRepository
import me.ibrahimsn.wallet.repository.EthereumNetworkRepository
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.BalanceUtil
import me.ibrahimsn.wallet.util.Constants
import me.ibrahimsn.wallet.util.RxBus
import java.math.BigInteger
import javax.inject.Inject

class WalletViewModel @Inject constructor(private val networkRepository: EthereumNetworkRepository,
                                          private val etherScanRepository: EtherScanRepository,
                                          walletRepository: WalletRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val currentWallet: MutableLiveData<Wallet?> = MutableLiveData()
    val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()
    val walletBalance: MutableLiveData<String> = MutableLiveData()
    val walletBalanceReal: MutableLiveData<String> = MutableLiveData()
    val ethPriceUsd: MutableLiveData<String> = MutableLiveData()

    /**
     * Asynchronously fetch current wallet on initialization
     */
    init {
        disposable.add(walletRepository.getCurrentWallet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(this::fetchWalletBalance)
                .doOnSuccess(this::fetchTransaction)
                .subscribe(this::onFetchCurrentWallet, this::onRxError))

        // Listen wallet changes
        disposable.add(RxBus.listen(RxBus.RxEvent.OnChangeCurrentWallet::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { Observable.just(it.wallet) }
                .doOnNext(this::onFetchCurrentWallet)
                .doOnNext(this::fetchWalletBalance)
                .doOnNext(this::fetchTransaction)
                .doOnError(this::onRxError)
                .subscribe())
    }

    /**
     * Asynchronously fetch wallet transactions via EtherScan API.
     */
    private fun fetchTransaction(wallet: Wallet?) {
        if (wallet != null)
            disposable.add(etherScanRepository.fetchTransaction(wallet.address, 1, 3)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onFetchTransactions, this::onRxError))
    }

    /**
     * Asynchronously fetch current wallet balance from ETH network.
     */
    private fun fetchWalletBalance(wallet: Wallet?) {
        if (wallet != null)
            disposable.add(networkRepository.getWalletBalance(wallet)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(this::fetchEthPrice)
                    .subscribe(this::onFetchWalletBalance, this::onRxError))
    }

    /**
     * Asynchronously fetch ETH price in USD
     */
    private fun fetchEthPrice(balance: BigInteger) {
        disposable.add(etherScanRepository.fetchEthPrice()
                .flatMap<Pair<EtherPriceResponse, BigInteger>> { Single.just(Pair(it, balance)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFetchEthPrice, this::onRxError))
    }

    private fun onFetchCurrentWallet(wallet: Wallet?) {
        currentWallet.postValue(wallet)
    }

    private fun onFetchWalletBalance(balance: BigInteger) {
        walletBalance.postValue(BalanceUtil.subunitToBase(balance).toString())
    }

    private fun onFetchTransactions(response: EtherScanResponse) {
        transactions.postValue(response.result)
    }

    private fun onFetchEthPrice(response: Pair<EtherPriceResponse, BigInteger>) {
        walletBalanceReal.postValue(BalanceUtil.ethToUsd(response.first.result.ethusd, response.second))
        ethPriceUsd.postValue(response.first.result.ethusd)
    }

    private fun onRxError(e: Throwable) {
        Log.d(Constants.TAG, "RxError:", e)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}