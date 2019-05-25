package me.ibrahimsn.wallet.di.module

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import me.ibrahimsn.wallet.di.util.ViewModelFactory

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    /*
     @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    internal abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel
     */
}