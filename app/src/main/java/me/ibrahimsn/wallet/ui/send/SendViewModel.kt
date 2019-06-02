package me.ibrahimsn.wallet.ui.send

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.GasSettings
import me.ibrahimsn.wallet.entity.TransactionSummary
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.EthereumNetworkRepository
import me.ibrahimsn.wallet.repository.PasswordRepository
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.BalanceUtil
import me.ibrahimsn.wallet.util.Constants
import java.math.BigDecimal
import java.math.BigInteger
import java.util.regex.Pattern
import javax.inject.Inject

class SendViewModel @Inject constructor(private val passwordRepository: PasswordRepository,
                                        private val networkRepository: EthereumNetworkRepository,
                                        walletRepository: WalletRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    val currentWallet: MutableLiveData<Wallet> = MutableLiveData()
    val transactionStatus: MutableLiveData<Boolean> = MutableLiveData()
    val transactionSummary: MutableLiveData<TransactionSummary> = MutableLiveData()

    val gasSettings = GasSettings(BigInteger(Constants.DEFAULT_GAS_PRICE), Constants.DEFAULT_GAS_LIMIT.toLong())

    init {
        disposable.add(walletRepository.getCurrentWallet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFetchCurrentWallet, this::onRxError))
    }

    fun createTransaction(from: Wallet, to: String, subUnitAmount: BigInteger) {
        if (from.isWallet)
            transactionSummary.value = TransactionSummary(from, to, subUnitAmount, calculateNetworkFee())
    }

    fun confirmTransaction() {
        if (transactionSummary.value != null) {
            val transaction = transactionSummary.value!!
            disposable.add(passwordRepository.getPassword(transaction.from)
                    .flatMap { password ->
                        networkRepository.createTransaction(transaction.from, transaction.to, transaction.amount,
                                gasSettings.gasPrice, gasSettings.gasLimit, password)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onCreateTransaction, this::onRxError))
        }
    }

    private fun onFetchCurrentWallet(wallet: Wallet?) {
        currentWallet.postValue(wallet)
    }

    private fun onCreateTransaction(hash: String) {
        transactionStatus.postValue(true)
    }

    private fun onRxError(e: Throwable) {
        Log.d(Constants.TAG, "RxError:", e)
        transactionStatus.postValue(false)
    }

    private fun calculateNetworkFee(): BigDecimal {
        return BalanceUtil.weiToEth(gasSettings.gasPrice.multiply(gasSettings.gasLimit.toBigInteger()))
    }
}