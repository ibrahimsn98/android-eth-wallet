package me.ibrahimsn.wallet.ui.send.send

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_send.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.ui.home.HomeActivity
import me.ibrahimsn.wallet.ui.send.SendActivity
import me.ibrahimsn.wallet.ui.send.SendViewModel
import me.ibrahimsn.wallet.util.BalanceUtil
import me.ibrahimsn.wallet.util.Constants
import javax.inject.Inject

class SendFragment : BaseFragment<SendActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SendViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_send
    }

    private var currentWallet: Wallet? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SendViewModel::class.java)
        activity.setTitle("Send Ethereum")

        btSend.setOnClickListener {
            if (currentWallet != null) {
                val receiver = etReceiverAddress.text.toString().trim()
                val amount = BalanceUtil.baseToSubunit(etAmount.text.toString().trim())

                val gasSettings = viewModel.gasSettings

                viewModel.createTransaction(currentWallet!!, receiver, amount,
                        gasSettings.gasPrice, gasSettings.gasLimit)
            }
        }

        viewModel.currentWallet.observe(this, Observer {
            currentWallet = it
        })
    }
}