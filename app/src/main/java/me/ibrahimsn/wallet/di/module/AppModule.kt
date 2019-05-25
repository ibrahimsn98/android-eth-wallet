package me.ibrahimsn.wallet.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import me.ibrahimsn.wallet.manager.GethAccountManager
import me.ibrahimsn.wallet.repository.EthereumNetworkRepository
import me.ibrahimsn.wallet.repository.PreferencesRepository
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.LogInterceptor
import okhttp3.OkHttpClient
import java.io.File
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
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(LogInterceptor())
                .build()
    }

    @Singleton
    @Provides
    internal fun provideGethAccountManager(context: Context): GethAccountManager {
        val file = File(context.filesDir, "keystore/keystore")
        return GethAccountManager(file)
    }

    @Singleton
    @Provides
    internal fun providePreferencesRepository(context: Context): PreferencesRepository {
        return PreferencesRepository(context)
    }

    @Singleton
    @Provides
    internal fun provideEthereumNetworkRepository(preferencesRepository: PreferencesRepository): EthereumNetworkRepository {
        return EthereumNetworkRepository(preferencesRepository)
    }

    @Singleton
    @Provides
    internal fun provideWalletRepository(gethAccountManager: GethAccountManager,
                                         preferencesRepository: PreferencesRepository,
                                         networkRepository: EthereumNetworkRepository,
                                         okHttpClient: OkHttpClient): WalletRepository {
        return WalletRepository(gethAccountManager, preferencesRepository, networkRepository, okHttpClient)
    }


}