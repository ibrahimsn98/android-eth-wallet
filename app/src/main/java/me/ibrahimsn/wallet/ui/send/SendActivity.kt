package me.ibrahimsn.wallet.ui.send

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_home.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseActivity

class SendActivity : BaseActivity() {

    lateinit var navController: NavController

    override fun layoutRes(): Int {
        return R.layout.activity_send
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = Navigation.findNavController(this, R.id.host)
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }
}