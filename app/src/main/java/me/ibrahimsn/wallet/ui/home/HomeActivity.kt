package me.ibrahimsn.wallet.ui.home

import android.os.Bundle
import android.view.Menu
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_home.*
import me.ibrahimsn.lib.NiceBottomBar
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseActivity

class HomeActivity : BaseActivity(), NavController.OnDestinationChangedListener {

    lateinit var navController: NavController

    override fun layoutRes(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        navController = Navigation.findNavController(this, R.id.host)
        navController.addOnDestinationChangedListener(this)

        bottomBar.setBottomBarCallback(object: NiceBottomBar.BottomBarCallback {
            override fun onItemSelect(pos: Int) {
                when (pos) {
                    0 -> navController.navigate(R.id.walletFragment)
                    1 -> navController.navigate(R.id.transactionsFragment)
                    2 -> navController.navigate(R.id.menuFragment)
                }
            }

            override fun onItemReselect(pos: Int) {

            }
        })
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when (destination.id) {
            R.id.walletFragment -> bottomBar.setActiveItem(0)
            R.id.transactionsFragment -> bottomBar.setActiveItem(1)
            R.id.menuFragment -> bottomBar.setActiveItem(2)
        }
    }

    override fun onBackPressed() {
        when (navController.currentDestination!!.id) {
            R.id.walletFragment -> finish()
            else ->  super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(this)
    }
}