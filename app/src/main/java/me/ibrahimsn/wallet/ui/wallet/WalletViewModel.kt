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
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WalletViewModel @Inject constructor(private val walletRepository: WalletRepository,
                                          private val transactionRepository: TransactionRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val wallets: MutableLiveData<List<Wallet>> = MutableLiveData()
    val defaultWallet: MutableLiveData<Wallet> = MutableLiveData()
    val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()

    init {
        Single.fromCallable {
            walletRepository.importAddress("deneme", "0xB81Ff63c6f096247dc6990555ED2943A284b4eD4")
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe()


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

        disposable.add(walletRepository.getDefaultWallet().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Wallet>() {
                    override fun onSuccess(t: Wallet) {
                        Log.d(Constants.TAG, "Get default wallet: ${t.address}")
                        defaultWallet.postValue(t)
                        fetchTransaction(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.d(Constants.TAG, "Get default wallet error:", e)
                    }
                }))
    }

    fun fetchTransaction(wallet: Wallet) {
        disposable.add(Observable.interval(0, 10L, TimeUnit.SECONDS).doOnNext {
            disposable.add(transactionRepository.fetchTransaction(wallet.address)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        transactions.postValue(it.result)
                    })
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }

    fun setDefaultWallet(wallet: Wallet) {
        walletRepository.setDefaultWallet(wallet)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}