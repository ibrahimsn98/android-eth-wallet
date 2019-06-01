package me.ibrahimsn.wallet.ui.addWallet.createWallet

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.Constants
import javax.inject.Inject

class CreateWalletViewModel @Inject constructor(private val walletRepository: WalletRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    val status: MutableLiveData<Boolean> = MutableLiveData()
    val stateLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun createWallet(name: String) {
        stateLoading.value = true

        disposable.add(walletRepository.createWallet(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCreateWallet, this::onRxError))
    }

    private fun onCreateWallet(wallet: Wallet) {
        status.postValue(true)
        stateLoading.postValue(false)
    }

    private fun onRxError(e: Throwable) {
        Log.d(Constants.TAG, "Error:", e)
        status.postValue(false)
        stateLoading.postValue(false)
    }
}