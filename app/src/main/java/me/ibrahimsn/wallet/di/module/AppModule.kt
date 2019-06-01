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
        return OkHttpClient.Builder().build()
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
    internal fun provideGson(): Gson {
        return Gson()
    }
}