package me.ibrahimsn.wallet.ui.menu

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_menu.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.home.HomeActivity
import javax.inject.Inject

class MenuFragment : BaseFragment<HomeActivity>(), DialogSelectNetwork.OnNetworkSelectedListener {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MenuViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_menu
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity, viewModelFactory).get(MenuViewModel::class.java)
        activity.setTitle("Preferences")

        val dialogNetworkSelect =  DialogSelectNetwork()
        dialogNetworkSelect.setOnNetworkSelectedListener(this)

        lyWallets.setOnClickListener {
            activity.navController.navigate(R.id.action_menuFragment_to_walletsFragment)
        }

        lyNetworks.setOnClickListener {
           dialogNetworkSelect.show(childFragmentManager, "select-network")
        }

        viewModel.currentWallet.observe(this, Observer {
            if (it != null)
                tvCurrentWallet.text = it.name
        })

        viewModel.defaultNetwork.observe(this, Observer {
            if (it != null)
                tvDefaultNetwork.text = it.name
        })
    }

    override fun onNetworkSelected(pos: Int) {
        viewModel.setDefaultNetworkInfo(pos)
    }
}