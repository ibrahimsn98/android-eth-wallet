package me.ibrahimsn.wallet.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import me.ibrahimsn.wallet.entity.Wallet

@Database(entities = [Wallet::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun walletDao(): WalletDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "App_database1").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}