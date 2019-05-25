package me.ibrahimsn.wallet.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import me.ibrahimsn.wallet.base.BaseApplication
import me.ibrahimsn.wallet.di.module.ActivityBuilder
import me.ibrahimsn.wallet.di.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = ([AndroidSupportInjectionModule::class, AppModule::class, ActivityBuilder::class]))
interface AppComponent : AndroidInjector<DaggerApplication> {

    /**
     * Inject application
     */
    fun inject(application: BaseApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}