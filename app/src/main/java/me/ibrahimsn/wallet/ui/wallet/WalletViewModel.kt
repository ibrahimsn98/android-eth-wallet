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
import me.ibrahimsn.wallet.util.Constants
import me.ibrahimsn.wallet.util.RxBus
import javax.inject.Inject

class WalletViewModel @Inject constructor(private val networkRepository: EthereumNetworkRepository,
                                          private val etherScanRepository: EtherScanRepository,
                                          walletRepository: WalletRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val currentWallet: MutableLiveData<Wallet?> = MutableLiveData()
    val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()
    val walletBalance: MutableLiveData<Double> = MutableLiveData()
    val walletBalanceReal: MutableLiveData<Double> = MutableLiveData()

    /**
     * Asynchronously fetch current wallet on initialization
     */
    init {
        disposable.add(walletRepository.getCurrentWallet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(this::onFetchCurrentWallet)
                .doOnSuccess(this::fetchWalletBalance)
                .doOnSuccess(this::fetchTransaction)
                .doOnError(this::onRxError)
                .subscribe())

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
            disposable.add(etherScanRepository.fetchTransaction(wallet.address, 1, 4)
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
    private fun fetchEthPrice(balance: Double) {
        disposable.add(etherScanRepository.fetchEthPrice()
                .flatMap<Pair<EtherPriceResponse, Double>> {
                    Single.just(Pair(it, balance))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(this::onFetchEthPrice)
                .subscribe())
    }

    private fun onFetchCurrentWallet(wallet: Wallet?) {
        currentWallet.postValue(wallet)
    }

    private fun onFetchWalletBalance(balance: Double) {
        walletBalance.postValue(balance)
    }

    private fun onFetchTransactions(response: EtherScanResponse) {
        transactions.postValue(response.result)
    }

    private fun onFetchEthPrice(response: Pair<EtherPriceResponse, Double>) {
        val price = response.first.result.ethusd.toDouble()
        walletBalanceReal.postValue(price * response.second)
    }

    private fun onRxError(e: Throwable) {
        Log.d(Constants.TAG, "RxError:", e)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}