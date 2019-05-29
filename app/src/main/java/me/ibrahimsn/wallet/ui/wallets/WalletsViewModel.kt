package me.ibrahimsn.wallet.ui.wallets

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.PreferencesRepository
import me.ibrahimsn.wallet.repository.WalletRepository
import javax.inject.Inject

class WalletsViewModel @Inject constructor(walletRepository: WalletRepository,
                                           private val preferencesRepository: PreferencesRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    val wallets = walletRepository.fetchWallets()

    fun setCurrentWallet(wallet: Wallet) {
        preferencesRepository.setCurrentWalletAddress(wallet.address)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}