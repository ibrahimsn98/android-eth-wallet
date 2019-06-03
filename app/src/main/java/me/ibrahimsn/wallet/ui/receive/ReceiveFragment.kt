package me.ibrahimsn.wallet.ui.receive

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_receive.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.home.HomeActivity
import javax.inject.Inject

class ReceiveFragment : BaseFragment<HomeActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ReceiveViewModel

    private var address = ""

    override fun layoutRes(): Int {
        return R.layout.fragment_receive
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReceiveViewModel::class.java)
        activity.setTitle(getString(R.string.receive_eth))

        ibCopyAddress.setOnClickListener {
            if (address != "") {
                copyToClipboard(address)
                Toast.makeText(activity, R.string.toast_address_copied, Toast.LENGTH_SHORT).show()
            }
        }

        btShareAddress.setOnClickListener {
            if (address != "")
                startActivity(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, address)
                    type = "text/plain"
                })
        }

        viewModel.currentWallet.observe(this, Observer {
            if (it != null) {
                address = it.address
                etAddress.setText(it.address)
            }
        })

        viewModel.walletQR.observe(this, Observer {
            if (it != null)
                Glide.with(this).load(it).into(ivBarcode)
        })
    }

    private fun copyToClipboard(data: String) {
        val clipboard = activity.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        if (clipboard != null)
            clipboard.primaryClip = ClipData.newPlainText("eth-address", data)
    }
}