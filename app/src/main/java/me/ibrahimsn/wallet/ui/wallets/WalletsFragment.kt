package me.ibrahimsn.wallet.ui.wallets

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.fragment_wallets.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.home.HomeActivity
import me.ibrahimsn.wallet.ui.importWallet.ImportWalletActivity
import javax.inject.Inject

class WalletsFragment : BaseFragment<HomeActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: WalletsViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_wallets
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity, viewModelFactory).get(WalletsViewModel::class.java)

        val walletAdapter = WalletAdapter()

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
}