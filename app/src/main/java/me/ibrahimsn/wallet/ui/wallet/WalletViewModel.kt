package me.ibrahimsn.wallet.ui.wallet

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.EtherScanResponse
import me.ibrahimsn.wallet.entity.Transaction
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.EtherScanRepository
import me.ibrahimsn.wallet.repository.EthereumNetworkRepository
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.Constants
import java.math.BigInteger
import javax.inject.Inject

class WalletViewModel @Inject constructor(walletRepository: WalletRepository,
                                          private val networkRepository: EthereumNetworkRepository,
                                          private val etherScanRepository: EtherScanRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val currentWallet: MutableLiveData<Wallet?> = MutableLiveData()
    val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()
    val walletBalance: MutableLiveData<BigInteger> = MutableLiveData()

    /**
     * Asynchronously get current wallet on initialization.
     */
    init {
        disposable.add(walletRepository.getCurrentWallet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(this::onGetCurrentWallet)
                .doOnSuccess(this::loadWalletBalance)
                .doOnSuccess(this::fetchTransactions)
                .doOnError(this::onRxError)
                .subscribe())
    }

    /**
     * Asynchronously fetch wallet transactions via EtherScan API.
     */
    private fun fetchTransactions(wallet: Wallet?) {
        if (wallet != null)
            disposable.add(etherScanRepository.fetchTransaction(wallet.address, 1, 6)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onFetchTransactions, this::onRxError))
    }

    /**
     * Asynchronously load current wallet balance from ETH network.
     */
    private fun loadWalletBalance(wallet: Wallet?) {
        if (wallet != null)
            disposable.add(networkRepository.getWalletBalance(wallet)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onLoadWalletBalance, this::onRxError))
    }

    private fun onGetCurrentWallet(wallet: Wallet?) {
        currentWallet.postValue(wallet)
    }

    private fun onLoadWalletBalance(balance: BigInteger) {
        walletBalance.postValue(balance)
    }

    private fun onFetchTransactions(response: EtherScanResponse) {
        transactions.postValue(response.result)
    }

    private fun onRxError(e: Throwable) {
        Log.d(Constants.TAG, "RxError:", e)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}