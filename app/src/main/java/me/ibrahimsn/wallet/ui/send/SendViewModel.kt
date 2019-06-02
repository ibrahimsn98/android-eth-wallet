package me.ibrahimsn.wallet.ui.send

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.EthereumNetworkRepository
import me.ibrahimsn.wallet.repository.PasswordRepository
import me.ibrahimsn.wallet.repository.PreferencesRepository
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.Constants
import java.math.BigInteger
import javax.inject.Inject

class SendViewModel @Inject constructor(private val passwordRepository: PasswordRepository,
                                        private val networkRepository: EthereumNetworkRepository,
                                        private val walletRepository: WalletRepository,
                                        preferencesRepository: PreferencesRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    val currentWallet: MutableLiveData<Wallet> = MutableLiveData()
    val gasSettings = preferencesRepository.getGasSettings()

    init {
        disposable.add(walletRepository.getCurrentWallet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFetchCurrentWallet, this::onRxError))
    }
// 0x1c45933cddf282b57a6c94a6fb65ac5594bfc7c0
    fun createTransaction(from: Wallet, to: String, subUnitAmount: BigInteger, gasPrice: BigInteger, gasLimit: Long) {
        if (from.isWallet) {
            disposable.add(passwordRepository.getPassword(from)
                    .flatMap { password ->
                        networkRepository.createTransaction(from, to, subUnitAmount, gasPrice, gasLimit, password)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onCreateTransaction, this::onRxError))
        } else {
            Log.d("###", "Not your wallet!")
        }
    }

    private fun onFetchCurrentWallet(wallet: Wallet?) {
        currentWallet.postValue(wallet)
    }

    private fun onCreateTransaction(hash: String) {
        Log.d("###", "asd: $hash")
    }

    private fun onRxError(e: Throwable) {
        Log.d(Constants.TAG, "RxError:", e)
    }
}