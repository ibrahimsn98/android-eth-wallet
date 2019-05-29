package me.ibrahimsn.wallet.ui.receive

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.fragment_receive.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.home.HomeActivity
import javax.inject.Inject

class ReceiveFragment : BaseFragment<HomeActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ReceiveViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_receive
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity, viewModelFactory).get(ReceiveViewModel::class.java)

        viewModel.currentWallet.observe(this, Observer {
            if (it != null)
                etAddress.setText(it.address)
        })

        viewModel.walletQR.observe(this, Observer {
            if (it != null)
                Glide.with(this).load(it).into(ivBarcode)
        })
    }
}