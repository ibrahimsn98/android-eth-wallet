package me.ibrahimsn.wallet.ui.wallet

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.Constants
import javax.inject.Inject

class WalletViewModel @Inject constructor(walletRepository: WalletRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val wallets: MutableLiveData<List<Wallet>> = MutableLiveData()

    init {
        disposable.add(walletRepository.fetchWallets().subscribeWith(object: DisposableSingleObserver<List<Wallet>>() {
            override fun onSuccess(data: List<Wallet>) {
                wallets.postValue(data)
            }

            override fun onError(e: Throwable) {
                Log.d(Constants.TAG, "Fetch wallets error:", e)
            }
        }))
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}