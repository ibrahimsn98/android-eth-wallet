package me.ibrahimsn.wallet.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.ibrahimsn.wallet.ui.addWallet.AddWalletActivity
import me.ibrahimsn.wallet.ui.home.HomeActivity
import me.ibrahimsn.wallet.ui.main.MainActivity
import me.ibrahimsn.wallet.ui.send.SendActivity

@Module
abstract class ActivityBuilder {

    /**
     * Build Main Activity
     */
    @ContributesAndroidInjector
    internal abstract fun buildMainActivity(): MainActivity

    /**
     * Build Home Activity
     */
    @ContributesAndroidInjector(modules = [HomeFragmentBuilder::class])
    internal abstract fun buildHomeActivity(): HomeActivity

    /**
     * Build Add Wallet Activity
     */
    @ContributesAndroidInjector(modules = [AddWalletFragmentBuilder::class])
    internal abstract fun buildAddWalletActivity(): AddWalletActivity

    /**
     * Build Send Transaction Activity
     */
    @ContributesAndroidInjector(modules = [SendFragmentBuilder::class])
    internal abstract fun buildSendActivity(): SendActivity
}