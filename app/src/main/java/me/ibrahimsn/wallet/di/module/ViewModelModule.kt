package me.ibrahimsn.wallet.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.ibrahimsn.wallet.di.util.ViewModelFactory
import me.ibrahimsn.wallet.di.util.ViewModelKey
import me.ibrahimsn.wallet.ui.wallet.WalletViewModel

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(WalletViewModel::class)
    internal abstract fun bindWalletViewModel(viewModel: WalletViewModel): ViewModel
}
