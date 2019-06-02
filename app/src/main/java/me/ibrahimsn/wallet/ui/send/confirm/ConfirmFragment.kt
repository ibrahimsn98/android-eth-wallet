package me.ibrahimsn.wallet.ui.send.confirm

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.send.SendActivity
import me.ibrahimsn.wallet.ui.send.SendViewModel
import javax.inject.Inject

class ConfirmFragment : BaseFragment<SendActivity>() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SendViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_confirm
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SendViewModel::class.java)
        activity.setTitle("Verify Transaction")
    }
}