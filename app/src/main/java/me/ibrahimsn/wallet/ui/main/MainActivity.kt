package me.ibrahimsn.wallet.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import me.ibrahimsn.wallet.ui.addWallet.AddWalletActivity
import me.ibrahimsn.wallet.ui.home.HomeActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.walletCount.observe(this, Observer {
            if (it != null) {
                startActivity(Intent(this, HomeActivity::class.java))

                if (it == 0)
                    startActivity(Intent(this, AddWalletActivity::class.java))

                finish()
            }
        })
    }
}

