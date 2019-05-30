package me.ibrahimsn.wallet.ui.addWallet.createWallet

import android.os.Bundle
import android.view.View
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.addWallet.AddWalletActivity

class CreateWalletFragment : BaseFragment<AddWalletActivity>() {

    override fun layoutRes(): Int {
        return R.layout.fragment_create_wallet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}