package me.ibrahimsn.wallet.ui.send.confirm

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_confirm.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.send.SendActivity
import me.ibrahimsn.wallet.ui.send.SendViewModel
import me.ibrahimsn.wallet.util.BalanceUtil
import javax.inject.Inject

class ConfirmFragment : BaseFragment<SendActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SendViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_confirm
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(activity, viewModelFactory).get(SendViewModel::class.java)
        activity.setTitle(getString(R.string.verify_transaction))

        viewModel.transactionSummary.observe(this, Observer {
            if (it != null) {
                val amount = BalanceUtil.subunitToBase(it.amount)
                val total = BalanceUtil.subunitToBase(it.amount).add(it.networkFee)

                tvFromName.text = it.from.name
                tvFromAddress.text = it.from.address
                tvTo.text = it.to

                tvAmount.text = StringBuilder()
                        .append(amount)
                        .append(" ETH")
                        .toString()

                tvNetworkFee.text = StringBuilder()
                        .append(it.networkFee)
                        .append(" ETH")
                        .toString()

                tvTotal.text = StringBuilder()
                        .append(total)
                        .append(" ETH")
                        .toString()
            }
        })

        btConfirm.setOnClickListener {
            viewModel.confirmTransaction()
        }

        viewModel.transactionStatus.observe(this, Observer {
            if (it != null) {
                viewModel.transactionStatus.value = null
                if (it) {
                    Toast.makeText(activity, R.string.toast_transaction_sent, Toast.LENGTH_SHORT).show()
                    activity.finish()
                } else
                    Toast.makeText(activity, R.string.toast_something_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }
}