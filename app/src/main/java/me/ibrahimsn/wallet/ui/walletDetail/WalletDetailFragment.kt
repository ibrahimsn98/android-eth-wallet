package me.ibrahimsn.wallet.ui.walletDetail

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_wallet_detail.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.home.HomeActivity
import javax.inject.Inject

class WalletDetailFragment : BaseFragment<HomeActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: WalletDetailViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_wallet_detail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(activity, viewModelFactory).get(WalletDetailViewModel::class.java)
        activity.setTitle("Wallet Detail")

        if (viewModel.wallet == null) {
            activity.navController.navigateUp()
            return
        }

        etWalletName.setText(viewModel.wallet!!.name)
    }
}