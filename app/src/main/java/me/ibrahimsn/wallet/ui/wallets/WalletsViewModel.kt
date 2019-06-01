package me.ibrahimsn.wallet.ui.wallets

import android.arch.lifecycle.ViewModel
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.PreferencesRepository
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.RxBus
import javax.inject.Inject

class WalletsViewModel @Inject constructor(walletRepository: WalletRepository,
                                           private val preferencesRepository: PreferencesRepository) : ViewModel() {

    val wallets = walletRepository.fetchWallets()

    /**
     * Update current wallet address stored in sharedPreferences
     * Publish update event information to Rx subscribers
     */
    fun setCurrentWallet(wallet: Wallet) {
        preferencesRepository.setCurrentWalletAddress(wallet.address)
        RxBus.publish(RxBus.RxEvent.OnChangeCurrentWallet(wallet))
    }
}