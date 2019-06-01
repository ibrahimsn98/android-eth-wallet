package me.ibrahimsn.wallet.ui.addWallet

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_add_wallet.*
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

        ibBack.setOnClickListener { onBackPressed() }
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    override fun onBackPressed() {
        if (navController.currentDestination!!.id == R.id.addWalletFragment)
            finish()
        else  super.onBackPressed()
    }
}