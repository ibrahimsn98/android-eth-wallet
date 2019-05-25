package me.ibrahimsn.wallet.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import me.ibrahimsn.wallet.di.component.DaggerAppComponent

class BaseApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel("Wallet", "Wallet",
                    NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val component = DaggerAppComponent.builder().application(this).build()
        component.inject(this)

        return component
    }
}