package me.ibrahimsn.wallet.ui.wallets

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.yarolegovich.discretescrollview.transform.ScaleTransformer
import kotlinx.android.synthetic.main.fragment_wallets.walletPicker
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.home.HomeActivity
import me.ibrahimsn.wallet.ui.wallet.WalletAdapter
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

        walletPicker.setItemTransformer(ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build())

        walletPicker.setSlideOnFling(true)
        walletPicker.setOffscreenItems(5)
        walletPicker.adapter = walletAdapter

        viewModel.wallets.observe(this, Observer {
            if (it != null)
                walletAdapter.setItems(it)
        })
    }
}