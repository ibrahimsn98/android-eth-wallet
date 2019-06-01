package me.ibrahimsn.wallet.ui.addWallet.createWallet

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_create_wallet.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.addWallet.AddWalletActivity
import javax.inject.Inject

class CreateWalletFragment : BaseFragment<AddWalletActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CreateWalletViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_create_wallet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateWalletViewModel::class.java)

        btCreate.setOnClickListener {
            val name = etWalletName.text.toString().trim()

            if (validateForm(name))
                viewModel.createWallet(name)
        }

        viewModel.status.observe(this, Observer {
            if (it != null)
                if (it)
                    activity.navController.navigateUp()
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