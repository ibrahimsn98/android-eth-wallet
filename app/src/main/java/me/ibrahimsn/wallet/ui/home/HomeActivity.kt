package me.ibrahimsn.wallet.ui.home

import android.os.Bundle
import androidx.navigation.NavController
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseActivity

class HomeActivity : BaseActivity() {

    private lateinit var navController: NavController

    override fun layoutRes(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}