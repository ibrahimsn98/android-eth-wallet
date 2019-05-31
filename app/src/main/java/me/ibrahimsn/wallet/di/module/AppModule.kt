package me.ibrahimsn.wallet.di.module

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import me.ibrahimsn.wallet.manager.GethAccountManager
import me.ibrahimsn.wallet.repository.*
import me.ibrahimsn.wallet.room.AppDatabase
import me.ibrahimsn.wallet.room.WalletDao
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
    internal fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    internal fun provideWalletDao(appDatabase: AppDatabase): WalletDao {
        return appDatabase.walletDao()
    }

    @Singleton
    @Provides
    internal fun provideGethAccountManager(context: Context): GethAccountManager {
        val file = File(context.filesDir, "keystore/keystore")
        return GethAccountManager(file)
    }

    @Singleton
    @Provides
    internal fun providePasswordRepository(context: Context): PasswordRepository {
        return PasswordRepository(context)
    }

    @Singleton
    @Provides
    internal fun providePreferencesRepository(context: Context): PreferencesRepository {
        return PreferencesRepository(context)
    }

    @Singleton
    @Provides
    internal fun provideEthereumNetworkRepository(preferencesRepository: PreferencesRepository,
                                                  accountManager: GethAccountManager): EthereumNetworkRepository {
        return EthereumNetworkRepository(preferencesRepository, accountManager)
    }

    @Singleton
    @Provides
    internal fun provideEtherScanRepository(okHttpClient: OkHttpClient,
                                            networkRepository: EthereumNetworkRepository): EtherScanRepository {
        return EtherScanRepository(okHttpClient, Gson(), networkRepository)
    }

    @Singleton
    @Provides
    internal fun provideWalletRepository(gethAccountManager: GethAccountManager,
                                         walletDao: WalletDao,
                                         passwordRepository: PasswordRepository,
                                         preferencesRepository: PreferencesRepository): WalletRepository {
        return WalletRepository(gethAccountManager, walletDao, passwordRepository, preferencesRepository)
    }
}