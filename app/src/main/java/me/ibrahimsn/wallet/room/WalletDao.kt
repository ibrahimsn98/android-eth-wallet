package me.ibrahimsn.wallet.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Single
import me.ibrahimsn.wallet.entity.Wallet

@Dao
interface WalletDao {

    @Query("SELECT * from wallets ORDER BY id ASC")
    fun getAll(): Single<List<Wallet>>

    @Insert
    fun insert(wallet: Wallet): Long

    @Query("DELETE from wallets WHERE address = :address")
    fun delete(address: String)

    @Query("SELECT * FROM wallets WHERE address = :address")
    fun find(address: String): Single<Wallet?>

    @Query("DELETE FROM wallets")
    fun deleteAll()
}