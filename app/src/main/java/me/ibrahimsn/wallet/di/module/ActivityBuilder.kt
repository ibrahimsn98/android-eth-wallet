package me.ibrahimsn.wallet.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import me.ibrahimsn.wallet.ui.home.HomeActivity
import me.ibrahimsn.wallet.ui.main.MainActivity

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
}