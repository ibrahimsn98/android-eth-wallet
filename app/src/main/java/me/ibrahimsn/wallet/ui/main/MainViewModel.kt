package me.ibrahimsn.wallet.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.room.WalletDao
import javax.inject.Inject

class MainViewModel @Inject constructor(walletDao: WalletDao) : ViewModel() {

    private val disposable = CompositeDisposable()
    val walletCount: MutableLiveData<Int> = MutableLiveData()

    init {
        disposable.add(Single.fromCallable { walletDao.count() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(walletCount::postValue))
    }
}