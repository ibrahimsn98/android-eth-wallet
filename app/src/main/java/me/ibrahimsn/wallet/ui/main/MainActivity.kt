package me.ibrahimsn.wallet.ui.main

import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseActivity

class MainActivity : BaseActivity() {

    private val disposable = CompositeDisposable()

    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
}

