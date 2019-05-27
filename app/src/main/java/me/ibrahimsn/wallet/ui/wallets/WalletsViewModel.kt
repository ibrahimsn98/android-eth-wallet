package me.ibrahimsn.wallet.ui.wallets

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.Constants
import javax.inject.Inject

class WalletsViewModel @Inject constructor(walletRepository: WalletRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    val wallets: MutableLiveData<List<Wallet>> = MutableLiveData()

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
    }

}