package me.ibrahimsn.wallet.ui.wallet

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.Transaction
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.TransactionRepository
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.Constants
import java.math.BigInteger
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WalletViewModel @Inject constructor(private val walletRepository: WalletRepository,
                                          private val transactionRepository: TransactionRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val wallets: MutableLiveData<List<Wallet>> = MutableLiveData()
    val defaultWallet: MutableLiveData<Wallet> = MutableLiveData()
    val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()
    val walletBalance: MutableLiveData<Pair<Wallet, Double>> = MutableLiveData()

    init {
        disposable.add(walletRepository.fetchWallets().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<Wallet>>() {
                    override fun onSuccess(data: List<Wallet>) {
                        wallets.postValue(data)
                    }

                    override fun onError(e: Throwable) {
                        Log.d(Constants.TAG, "Fetch wallets error:", e)
                    }
                }))

        fetchTransaction(Wallet(""))
    }

    fun fetchTransaction(wallet: Wallet) {
        disposable.add(Observable.interval(0, 10L, TimeUnit.SECONDS).doOnNext {
            disposable.add(transactionRepository.fetchTransaction("0xc1E2Ec12849e4F1a30ab9988357E273a96951C0b", 1, 20)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        transactions.postValue(it.result)
                    })
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }

    fun loadWalletBalance(wallet: Wallet) {
        disposable.add(walletRepository.balanceInWei(wallet).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(object: DisposableSingleObserver<BigInteger>() {
                    override fun onSuccess(t: BigInteger) {
                        walletBalance.postValue(Pair(wallet, t.toDouble()))
                    }

                    override fun onError(e: Throwable) {
                        Log.d(Constants.TAG, "Load balance error:", e)
                    }
                }))
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}