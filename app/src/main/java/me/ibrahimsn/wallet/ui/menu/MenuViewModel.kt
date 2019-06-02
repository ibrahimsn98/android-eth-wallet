package me.ibrahimsn.wallet.ui.menu

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.NetworkInfo
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.EthereumNetworkRepository
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.Constants
import me.ibrahimsn.wallet.util.RxBus
import javax.inject.Inject

class MenuViewModel @Inject constructor(walletRepository: WalletRepository,
                                        private val networkRepository: EthereumNetworkRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    val currentWallet: MutableLiveData<Wallet> = MutableLiveData()
    val defaultNetwork: MutableLiveData<NetworkInfo> = MutableLiveData()

    /**
     * Asynchronously fetch current wallet and default network
     * Start to listen "current wallet" and "default network" changes
     */
    init {
        defaultNetwork.value = networkRepository.getDefaultNetwork()

        disposable.add(walletRepository.getCurrentWallet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onFetchCurrentWallet, this::onRxError))

        disposable.add(RxBus.listen(RxBus.RxEvent.OnChangeCurrentWallet::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { Observable.just(it.wallet) }
                .doOnNext(this::onFetchCurrentWallet)
                .doOnError(this::onRxError)
                .subscribe())

        disposable.add(RxBus.listen(RxBus.RxEvent.OnChangeDefaultNetwork::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { Observable.just(it.network) }
                .doOnNext(this::onFetchDefaultNetwork)
                .doOnError(this::onRxError)
                .subscribe())
    }

    fun setDefaultNetworkInfo(pos: Int) {
        networkRepository.setDefaultNetworkInfo(pos)
    }

    private fun onFetchCurrentWallet(wallet: Wallet?) {
        currentWallet.postValue(wallet)
    }

    private fun onFetchDefaultNetwork(network: NetworkInfo) {
        defaultNetwork.postValue(network)
    }

    private fun onRxError(e: Throwable) {
        Log.d(Constants.TAG, "RxError:", e)
    }
}