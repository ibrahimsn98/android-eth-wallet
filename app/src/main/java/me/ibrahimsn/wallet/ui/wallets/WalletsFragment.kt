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
import me.ibrahimsn.wallet.ui.home.HomeActivity
import me.ibrahimsn.wallet.ui.importWallet.ImportWalletActivity
import javax.inject.Inject

class WalletsFragment : BaseFragment<HomeActivity>(), WalletAdapter.WalletCallback {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: WalletsViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_wallets
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WalletsViewModel::class.java)
        setHasOptionsMenu(true)

        val walletAdapter = WalletAdapter(this)

        nestedScrollView.isNestedScrollingEnabled = true
        rvWallets.isNestedScrollingEnabled = false
        rvWallets.layoutManager = LinearLayoutManager(activity)
        rvWallets.adapter = walletAdapter

        btAdd.setOnClickListener {
            startActivity(Intent(activity, ImportWalletActivity::class.java))
        }

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
        WalletDialog().show(childFragmentManager, "Wallet-Dialog")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null && item.itemId == R.id.action_add)
            startActivity(Intent(activity, ImportWalletActivity::class.java))

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_wallets, menu)
    }
}