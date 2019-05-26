package me.ibrahimsn.wallet.ui.importWallet

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

class ImportWalletViewModel @Inject constructor(private val walletRepository: WalletRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val status: MutableLiveData<Boolean> = MutableLiveData()

    fun importPublicAddress(name: String, address: String) {
        disposable.add(walletRepository.importPublicAddress(name, address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onImportPublicAddress, this::onError))
    }

    private fun onImportPublicAddress(wallet: Wallet) {
        status.postValue(true)
    }

    private fun onError(e: Throwable) {
        Log.d(Constants.TAG, "Error:", e)
        status.postValue(false)
    }
}