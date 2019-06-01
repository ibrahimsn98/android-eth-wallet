package me.ibrahimsn.wallet.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.ibrahimsn.wallet.ui.menu.MenuFragment
import me.ibrahimsn.wallet.ui.receive.ReceiveFragment
import me.ibrahimsn.wallet.ui.transactions.TransactionsFragment
import me.ibrahimsn.wallet.ui.wallet.WalletFragment
import me.ibrahimsn.wallet.ui.walletDetail.WalletDetailFragment
import me.ibrahimsn.wallet.ui.wallets.WalletsFragment

@Module
abstract class HomeFragmentBuilder {

    /**
     * Build Wallet Fragment
     */
    @ContributesAndroidInjector
    internal abstract fun buildWalletFragment(): WalletFragment

    /**
     * Build Menu Fragment
     */
    @ContributesAndroidInjector
    internal abstract fun buildMenuFragment(): MenuFragment

    /**
     * Build Wallets Fragment
     */
    @ContributesAndroidInjector
    internal abstract fun buildWalletsFragment(): WalletsFragment

    /**
     * Build Wallet Detail Fragment
     */
    @ContributesAndroidInjector
    internal abstract fun buildWalletDetailFragment(): WalletDetailFragment

    /**
     * Build Receive Transaction Fragment
     */
    @ContributesAndroidInjector
    internal abstract fun buildReceiveFragment(): ReceiveFragment

    /**
     * Build Transactions Fragment
     */
    @ContributesAndroidInjector
    internal abstract fun buildTransactionsFragment(): TransactionsFragment
}