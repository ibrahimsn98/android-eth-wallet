package me.ibrahimsn.wallet.ui.addWallet.importWallet.importKeystore

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.PreferencesRepository
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.Constants
import me.ibrahimsn.wallet.util.RxBus
import javax.inject.Inject

class ImportKeystoreViewModel @Inject constructor(private val walletRepository: WalletRepository,
                                                  private val preferencesRepository: PreferencesRepository) : ViewModel() {

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

    private fun onImportKeystore(wallet: Wallet) {
        preferencesRepository.setCurrentWalletAddress(wallet.address)
        RxBus.publish(RxBus.RxEvent.OnChangeCurrentWallet(wallet))
        status.postValue(true)
    }

    private fun onRxError(e: Throwable) {
        Log.d(Constants.TAG, "Error:", e)
        status.postValue(false)
    }
}