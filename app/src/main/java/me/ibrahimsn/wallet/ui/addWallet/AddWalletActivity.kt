package me.ibrahimsn.wallet.ui.addWallet

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseActivity

class AddWalletActivity : BaseActivity() {

    lateinit var navController: NavController

    override fun layoutRes(): Int {
        return R.layout.activity_add_wallet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = Navigation.findNavController(this, R.id.host)
    }
}