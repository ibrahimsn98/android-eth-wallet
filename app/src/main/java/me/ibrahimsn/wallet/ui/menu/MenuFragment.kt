package me.ibrahimsn.wallet.ui.menu

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_menu.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.home.HomeActivity
import me.ibrahimsn.wallet.util.Constants
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
        activity.setTitle(getString(R.string.preferences))

        val dialogNetworkSelect =  DialogSelectNetwork()
        dialogNetworkSelect.setOnNetworkSelectedListener(this)

        lyWallets.setOnClickListener {
            activity.navController.navigate(R.id.action_menuFragment_to_walletsFragment)
        }

        lyNetworks.setOnClickListener {
           dialogNetworkSelect.show(childFragmentManager, "select-network")
        }

        lySourceCode.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GITHUB_REPO)))
        }

        lyReportBug.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GITHUB_REPO_ISSUES)))
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