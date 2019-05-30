package me.ibrahimsn.wallet.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.ibrahimsn.wallet.di.util.ViewModelFactory
import me.ibrahimsn.wallet.di.util.ViewModelKey
import me.ibrahimsn.wallet.ui.addWallet.importWallet.ImportWalletViewModel
import me.ibrahimsn.wallet.ui.receive.ReceiveViewModel
import me.ibrahimsn.wallet.ui.send.SendViewModel
import me.ibrahimsn.wallet.ui.transactions.TransactionsViewModel
import me.ibrahimsn.wallet.ui.wallet.WalletViewModel
import me.ibrahimsn.wallet.ui.wallets.WalletsViewModel

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(WalletViewModel::class)
    internal abstract fun bindWalletViewModel(viewModel: WalletViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImportWalletViewModel::class)
    internal abstract fun bindImportWalletViewModel(viewModel: ImportWalletViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WalletsViewModel::class)
    internal abstract fun bindWalletsViewModel(viewModel: WalletsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SendViewModel::class)
    internal abstract fun bindSendViewModel(viewModel: SendViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReceiveViewModel::class)
    internal abstract fun bindReceiveViewModel(viewModel: ReceiveViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionsViewModel::class)
    internal abstract fun bindTransactionsViewModel(viewModel: TransactionsViewModel): ViewModel
}
