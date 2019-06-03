package me.ibrahimsn.wallet.ui.addWallet.importWallet.importKeystore

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_import_keystore.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.addWallet.AddWalletActivity
import javax.inject.Inject

class ImportKeystoreFragment : BaseFragment<AddWalletActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ImportKeystoreViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_import_keystore
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ImportKeystoreViewModel::class.java)

        btImport.setOnClickListener {
            val name = etWalletName.text.toString().trim()
            val keystore = etWalletKeystore.text.toString().trim().toLowerCase()
            val password = etWalletPassword.text.toString().trim().toLowerCase()

            if (validateForm(name))
                viewModel.importKeystore(name, keystore, password)
        }

        viewModel.status.observe(this, Observer {
            if (it != null) {
                viewModel.status.value = null
                if (it) {
                    Toast.makeText(activity, R.string.toast_wallet_import, Toast.LENGTH_SHORT).show()
                    activity.finish()
                } else
                    Toast.makeText(activity, R.string.toast_something_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validateForm(name: String): Boolean {
        if (name.length < 3 || name.length > 30) {
            Toast.makeText(activity, R.string.toast_wallet_name_error, Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}