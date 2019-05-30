package me.ibrahimsn.wallet.ui.addWallet

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_add_wallet.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment

class AddWalletFragment : BaseFragment<AddWalletActivity>() {

    override fun layoutRes(): Int {
        return R.layout.fragment_add_wallet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btCreateWallet.setOnClickListener {
            activity.navController.navigate(R.id.action_addWalletFragment_to_createWalletFragment)
        }

        btImportWallet.setOnClickListener {
            activity.navController.navigate(R.id.action_addWalletFragment_to_importWalletFragment)
        }
    }
}