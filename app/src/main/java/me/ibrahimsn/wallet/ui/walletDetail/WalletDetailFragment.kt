package me.ibrahimsn.wallet.ui.walletDetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

        btSave.setOnClickListener {
            val newName = etWalletName.text.toString().trim()

            if (validateForm(newName))
                viewModel.updateWalletName(newName)
        }

        btDelete.setOnClickListener {
            viewModel.deleteWallet()
        }

        viewModel.status.observe(this, Observer {
            if (it != null) {
                viewModel.status.value = null
                if (it) {
                    Toast.makeText(activity, "Wallet has been updated!", Toast.LENGTH_SHORT).show()
                    activity.navController.navigateUp()
                } else
                    Toast.makeText(activity, "En error occurred while updating wallet!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validateForm(name: String): Boolean {
        if (name.length < 3 || name.length > 30) {
            Toast.makeText(activity, "Please enter a name!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}