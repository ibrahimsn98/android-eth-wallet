package me.ibrahimsn.wallet.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import me.ibrahimsn.wallet.repository.EthereumNetworkRepository
import me.ibrahimsn.wallet.repository.PreferencesRepository
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    internal fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    internal fun providePreferencesRepository(context: Context): PreferencesRepository {
        return PreferencesRepository(context)
    }

    @Singleton
    @Provides
    internal fun provideEthereumNetworkRepository(preferencesRepository: PreferencesRepository):EthereumNetworkRepository {
        return EthereumNetworkRepository(preferencesRepository)
    }
}