package me.ibrahimsn.wallet.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.ibrahimsn.wallet.ui.importWallet.ImportWalletActivity
import me.ibrahimsn.wallet.ui.menu.MenuFragment
import me.ibrahimsn.wallet.ui.wallet.WalletFragment
import me.ibrahimsn.wallet.ui.wallets.WalletsFragment

@Module
abstract class HomeFragmentBuilder {

    /**
     * Build Wallet Fragment
     */
    @ContributesAndroidInjector
    internal abstract fun buildWalletFragment(): WalletFragment

    /**
     * Build Import Wallet Fragment
     */
    @ContributesAndroidInjector
    internal abstract fun buildImportWalletFragment(): ImportWalletActivity

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
}