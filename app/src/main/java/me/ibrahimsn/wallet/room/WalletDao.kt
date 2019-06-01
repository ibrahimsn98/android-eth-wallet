package me.ibrahimsn.wallet.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import me.ibrahimsn.wallet.entity.Wallet

@Dao
interface WalletDao {

    @Query("SELECT * from wallets ORDER BY isWallet DESC, id ASC")
    fun getAll(): LiveData<List<Wallet>>

    @Query("SELECT * from wallets ORDER BY isWallet DESC, id ASC")
    fun getAllRx(): Single<List<Wallet>>

    @Insert
    fun insert(wallet: Wallet): Long

    @Delete
    fun delete(wallet: Wallet)

    @Query("DELETE from wallets WHERE address = :address")
    fun delete(address: String)

    @Query("SELECT * FROM wallets WHERE address = :address")
    fun find(address: String): Single<Wallet?>

    @Update
    fun update(wallet: Wallet)

    @Query("DELETE FROM wallets")
    fun deleteAll()
}