package me.ibrahimsn.wallet.ui.wallet

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_wallet.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.home.HomeActivity
import me.ibrahimsn.wallet.ui.send.SendActivity
import me.ibrahimsn.wallet.ui.transactions.TransactionAdapter
import java.lang.StringBuilder
import javax.inject.Inject

class WalletFragment : BaseFragment<HomeActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: WalletViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_wallet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity, viewModelFactory).get(WalletViewModel::class.java)
        activity.setTitle("Ethereum Wallet")

        val transactionAdapter = TransactionAdapter(activity)
        rvTransactions.layoutManager = LinearLayoutManager(activity)
        rvTransactions.adapter = transactionAdapter

        btSend.setOnClickListener {
            startActivity(Intent(activity, SendActivity::class.java))
        }

        btReceive.setOnClickListener {
            activity.navController.navigate(R.id.action_walletFragment_to_receiveFragment)
        }

        viewModel.currentWallet.observe(this, Observer {
            if (it != null) {
                transactionAdapter.setWalletAddress(it.address)
                tvName.text = it.name
            }
        })

        viewModel.transactions.observe(this, Observer {
            if (it != null) {
                transactionAdapter.setItems(it)
                renderScreen(it.size)
            }
        })

        viewModel.walletBalance.observe(this, Observer {
            if (it != null)
                tvBalance.text = StringBuilder().append(it).append(" ETH").toString()
        })

        viewModel.walletBalanceReal.observe(this, Observer {
            if (it != null)
                tvBalanceReal.text = StringBuilder().append(it).append(" USD").toString()
        })

        viewModel.ethPriceUsd.observe(this, Observer {
            if (it != null)
                transactionAdapter.setEthPriceUsd(it)
        })
    }

    private fun renderScreen(itemCount: Int) {
        pbLoading.visibility = View.GONE
        rvTransactions.visibility = if (itemCount > 0) View.VISIBLE else View.GONE
        lyEmpty.visibility = if (itemCount <= 0) View.VISIBLE else View.GONE
    }

    override fun onStart() {
        super.onStart()
        waveView.onStart()
    }

    override fun onStop() {
        super.onStop()
        waveView.onStop()
    }
}