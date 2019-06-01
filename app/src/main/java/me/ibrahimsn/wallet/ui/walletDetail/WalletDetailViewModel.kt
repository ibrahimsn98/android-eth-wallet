package me.ibrahimsn.wallet.ui.walletDetail

import android.arch.lifecycle.ViewModel
import me.ibrahimsn.wallet.entity.Wallet
import javax.inject.Inject

class WalletDetailViewModel @Inject constructor() : ViewModel() {

    var wallet: Wallet? = null

    fun updateWallet(name: String)
}