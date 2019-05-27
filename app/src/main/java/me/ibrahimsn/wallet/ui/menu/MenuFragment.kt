package me.ibrahimsn.wallet.ui.menu

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_menu.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.home.HomeActivity

class MenuFragment : BaseFragment<HomeActivity>() {

    override fun layoutRes(): Int {
        return R.layout.fragment_menu
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lyWallets.setOnClickListener {
            activity.navController.navigate(R.id.action_menuFragment_to_walletsFragment)
        }
    }
}