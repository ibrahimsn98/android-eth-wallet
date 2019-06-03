package me.ibrahimsn.wallet.ui.addWallet.importWallet.importAddress

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_import_address.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.addWallet.AddWalletActivity
import java.util.regex.Pattern
import javax.inject.Inject

class ImportAddressFragment : BaseFragment<AddWalletActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ImportAddressViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_import_address
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ImportAddressViewModel::class.java)

        btImport.setOnClickListener {
            val name = etWalletName.text.toString().trim()
            val address = etWalletAddress.text.toString().trim().toLowerCase()

            if (validateForm(name, address))
                viewModel.importPublicAddress(name, address)
        }

        viewModel.status.observe(this, Observer {
            if (it != null) {
                viewModel.status.value = null
                if (it) {
                    Toast.makeText(activity, "Wallet has been imported.", Toast.LENGTH_SHORT).show()
                    activity.finish()
                } else
                    Toast.makeText(activity, "Something went wrong.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validateForm(name: String, address: String): Boolean {
        if (name.length < 3 || name.length > 30) {
            Toast.makeText(activity, "Wallet name must have at least 3 characters.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Pattern.matches("^0x[a-fA-F0-9]{40}\$", address)) {
            Toast.makeText(activity, "Please enter a valid ethereum address.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}