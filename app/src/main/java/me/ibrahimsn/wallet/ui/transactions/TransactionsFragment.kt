package me.ibrahimsn.wallet.ui.transactions

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_transactions.*
import kotlinx.android.synthetic.main.fragment_transactions.lyEmpty
import kotlinx.android.synthetic.main.fragment_transactions.pbLoading
import kotlinx.android.synthetic.main.fragment_transactions.rvTransactions
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.home.HomeActivity
import javax.inject.Inject

class TransactionsFragment : BaseFragment<HomeActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TransactionsViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_transactions
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity, viewModelFactory).get(TransactionsViewModel::class.java)
        activity.setTitle("Last Transactions")

        val transactionAdapter = TransactionAdapter(activity)
        rvTransactions.layoutManager = LinearLayoutManager(activity)
        rvTransactions.adapter = transactionAdapter

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }

        viewModel.currentWallet.observe(this, Observer {
            if (it != null)
                transactionAdapter.setWalletAddress(it.address)
        })

        viewModel.transactions.observe(this, Observer {
            if (it != null) {
                transactionAdapter.setItems(it)
                renderScreen(it.size)
            }
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
}