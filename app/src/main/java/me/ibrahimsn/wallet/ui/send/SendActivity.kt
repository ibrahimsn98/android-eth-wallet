package me.ibrahimsn.wallet.ui.send

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseActivity
import javax.inject.Inject

class SendActivity : BaseActivity() {


    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: SendViewModel
    lateinit var navController: NavController

    override fun layoutRes(): Int {
        return R.layout.activity_send
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SendViewModel::class.java)

        navController = Navigation.findNavController(this, R.id.host)
    }
}