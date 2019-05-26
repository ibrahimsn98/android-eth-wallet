package me.ibrahimsn.wallet.ui.importWallet

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_import_wallet.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseActivity
import java.util.regex.Pattern
import javax.inject.Inject

class ImportWalletActivity : BaseActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ImportWalletViewModel

    override fun layoutRes(): Int {
        return R.layout.activity_import_wallet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ImportWalletViewModel::class.java)

        btImport.setOnClickListener {
            val name = etWalletName.text.toString()
            val address = etWalletAddress.text.toString()

            if (validateForm(name, address))
                viewModel.importPublicAddress(name, address)
        }

        viewModel.status.observe(this, Observer { status ->
            status?.let {
               if (it)
                   finish()
            }
        })
    }

    private fun validateForm(name: String, address: String): Boolean {
        if (!Pattern.matches("^0x[a-fA-F0-9]{40}\$", address)) {
            Toast.makeText(this, "Please enter a valid ethereum address!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}