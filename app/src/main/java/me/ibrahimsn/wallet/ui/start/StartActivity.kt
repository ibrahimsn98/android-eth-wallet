package me.ibrahimsn.wallet.ui.start

import android.os.Bundle
import androidx.navigation.NavController
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseActivity

class StartActivity : BaseActivity() {

    private lateinit var navController: NavController

    override fun layoutRes(): Int {
        return R.layout.activity_start
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}