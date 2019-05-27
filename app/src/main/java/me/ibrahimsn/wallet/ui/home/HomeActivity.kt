package me.ibrahimsn.wallet.ui.home

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_home.*
import me.ibrahimsn.lib.NiceBottomBar
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseActivity

class HomeActivity : BaseActivity() {

    lateinit var navController: NavController

    override fun layoutRes(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = Navigation.findNavController(this, R.id.host)

        bottomBar.setBottomBarCallback(object: NiceBottomBar.BottomBarCallback {
            override fun onItemSelect(pos: Int) {
                when (pos) {
                    0 -> navController.navigate(R.id.walletFragment)
                    3 -> navController.navigate(R.id.menuFragment)
                }
            }

            override fun onItemReselect(pos: Int) {

            }
        })
    }
}