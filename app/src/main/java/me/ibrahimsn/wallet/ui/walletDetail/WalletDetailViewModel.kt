package me.ibrahimsn.wallet.ui.walletDetail

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

class WalletDetailViewModel @Inject constructor(private val walletRepository: WalletRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    val status: MutableLiveData<Boolean> = MutableLiveData()

    var wallet: Wallet? = null

    /**
     * Asynchronously update wallet name stored in Room
     */
    fun updateWalletName(name: String) {
        if (wallet != null)
            disposable.add(walletRepository.updateWalletName(wallet!!, name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onUpdateWalletName, this::onRxError))
    }

    /**
     * Asynchronously delete wallet from both Room and GethAccountManager
     */
    fun deleteWallet() {
        if (wallet != null)
            disposable.add(walletRepository.deleteWallet(wallet!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onDeleteWallet, this::onRxError))
    }

    private fun onUpdateWalletName() {
        status.postValue(true)
    }

    private fun onDeleteWallet() {
        status.postValue(true)
    }

    private fun onRxError(e: Throwable) {
        Log.d(Constants.TAG, "Error:", e)
        status.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}