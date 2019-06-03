package me.ibrahimsn.wallet.ui.wallets

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.fragment_wallets.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.ui.addWallet.AddWalletActivity
import me.ibrahimsn.wallet.ui.home.HomeActivity
import me.ibrahimsn.wallet.ui.walletDetail.WalletDetailViewModel
import javax.inject.Inject

class WalletsFragment : BaseFragment<HomeActivity>(), WalletAdapter.WalletCallback {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: WalletsViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_wallets
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity, viewModelFactory).get(WalletsViewModel::class.java)
        activity.setTitle(getString(R.string.wallets))
        setHasOptionsMenu(true)

        val walletAdapter = WalletAdapter(this)

        rvWallets.layoutManager = LinearLayoutManager(activity)
        rvWallets.adapter = walletAdapter

        viewModel.wallets.observe(this, Observer {
            if (it != null)
                walletAdapter.setItems(it)
        })
    }

    override fun onWalletClicked(wallet: Wallet) {
        viewModel.setCurrentWallet(wallet)
        activity.navController.navigateUp()
    }

    override fun onMoreClicked(wallet: Wallet) {
        ViewModelProviders.of(activity, viewModelFactory)
                .get(WalletDetailViewModel::class.java).wallet = wallet
        activity.navController.navigate(R.id.action_walletsFragment_to_walletDetailFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null && item.itemId == R.id.action_add)
            startActivity(Intent(activity, AddWalletActivity::class.java))

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_wallets, menu)
    }
}