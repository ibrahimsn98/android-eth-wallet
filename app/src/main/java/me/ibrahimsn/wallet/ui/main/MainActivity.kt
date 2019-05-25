package me.ibrahimsn.wallet.ui.main

import android.content.Intent
import android.os.Bundle
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseActivity
import me.ibrahimsn.wallet.ui.home.HomeActivity

class MainActivity : BaseActivity() {

    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}

