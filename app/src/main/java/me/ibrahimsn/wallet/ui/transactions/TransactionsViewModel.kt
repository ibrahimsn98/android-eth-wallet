package me.ibrahimsn.wallet.ui.transactions

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.EtherPriceResponse
import me.ibrahimsn.wallet.entity.EtherScanResponse
import me.ibrahimsn.wallet.entity.Transaction
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.EtherScanRepository
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.Constants
import me.ibrahimsn.wallet.util.RxBus
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(private val etherScanRepository: EtherScanRepository,
                                                walletRepository: WalletRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    val currentWallet: MutableLiveData<Wallet> = MutableLiveData()
    val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()
    val ethPriceUsd: MutableLiveData<String> = MutableLiveData()

    /**
     * Asynchronously fetch current wallet from sharedPreferences on initialization
     */
    init {
        disposable.add(walletRepository.getCurrentWallet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(this::fetchTransaction)
                .subscribe(this::onFetchCurrentWallet, this::onRxError))

        // Listen wallet changes
        disposable.add(RxBus.listen(RxBus.RxEvent.OnChangeCurrentWallet::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { Observable.just(it.wallet) }
                .doOnNext(this::fetchTransaction)
                .doOnError(this::onRxError)
                .subscribe())

        disposable.add(etherScanRepository.fetchEthPrice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFetchEthPrice, this::onRxError))
    }

    /**
     * Asynchronously fetch wallet transactions via EtherScan API.
     */
    private fun fetchTransaction(wallet: Wallet?) {
        if (wallet != null)
            disposable.add(etherScanRepository.fetchTransaction(wallet.address, 1, 50)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onFetchTransaction, this::onRxError))
    }

    private fun onFetchCurrentWallet(wallet: Wallet?) {
        currentWallet.postValue(wallet)
    }

    private fun onFetchTransaction(response: EtherScanResponse) {
        transactions.postValue(response.result)
    }

    private fun onFetchEthPrice(response: EtherPriceResponse) {
        ethPriceUsd.postValue(response.result.ethusd)
    }

    private fun onRxError(e: Throwable) {
        Log.d(Constants.TAG, "RxError:", e)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}