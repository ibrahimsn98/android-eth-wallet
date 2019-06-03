package me.ibrahimsn.wallet.ui.addWallet.importWallet.importKeystore

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.Constants
import javax.inject.Inject

class ImportKeystoreViewModel @Inject constructor(private val walletRepository: WalletRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    val status: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * Asynchronously put new wallet into Room and GethAccountManager
     */
    fun importKeystore(name: String, keystore: String, backupPassword: String) {
        disposable.add(walletRepository.importKeystore(name, keystore, backupPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onImportKeystore, this::onRxError))
    }

    private fun onImportKeystore() {
        status.postValue(true)
    }

    private fun onRxError(e: Throwable) {
        Log.d(Constants.TAG, "Error:", e)
        status.postValue(false)
    }
}