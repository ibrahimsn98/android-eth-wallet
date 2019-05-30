package me.ibrahimsn.wallet.ui.send.send

import android.os.Bundle
import android.view.View
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.home.HomeActivity

class SendFragment : BaseFragment<HomeActivity>() {

    override fun layoutRes(): Int {
        return R.layout.fragment_send
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}