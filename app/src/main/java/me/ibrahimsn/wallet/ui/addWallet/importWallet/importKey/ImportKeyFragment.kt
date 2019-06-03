package me.ibrahimsn.wallet.ui.addWallet.importWallet.importKey

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_import_key.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.addWallet.AddWalletActivity
import java.util.regex.Pattern
import javax.inject.Inject

class ImportKeyFragment : BaseFragment<AddWalletActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ImportKeyViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_import_key
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ImportKeyViewModel::class.java)

        btImport.setOnClickListener {
            val name = etWalletName.text.toString().trim()
            val privateKey = etWalletPrivateKey.text.toString().trim().toLowerCase()

            if (validateForm(name, privateKey))
                viewModel.importPrivateKey(name, privateKey)
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

    private fun validateForm(name: String, privateKey: String): Boolean {
        if (name.length < 3 || name.length > 30) {
            Toast.makeText(activity, "Wallet name must have at least 3 characters.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Pattern.matches("^\\S{64}\$", privateKey)) {
            Toast.makeText(activity, "Please enter a valid private key!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}