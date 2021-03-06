package me.ibrahimsn.wallet.ui.receive

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.repository.WalletRepository
import me.ibrahimsn.wallet.util.Constants
import javax.inject.Inject

class ReceiveViewModel @Inject constructor(walletRepository: WalletRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    val currentWallet: MutableLiveData<Wallet?> = MutableLiveData()
    val walletQR: MutableLiveData<Bitmap?> = MutableLiveData()

    /**
     * Asynchronously fetch current wallet from sharedPreferences on initialization.
     */
    init {
        disposable.add(walletRepository.getCurrentWallet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(this::createWalletQR)
                .subscribe(this::onFetchCurrentWallet, this::onRxError))
    }

    /**
     * Asynchronously create QR code for wallet address
     */
    private fun createWalletQR(wallet: Wallet?) {
        if (wallet != null)
            disposable.add(qrImageGenerator(wallet.address)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onQRCreated, this::onRxError))
    }

    private fun onFetchCurrentWallet(wallet: Wallet?) {
        currentWallet.postValue(wallet)
    }

    private fun onQRCreated(qr: Bitmap?) {
        walletQR.postValue(qr)
    }

    private fun onRxError(e: Throwable) {
        Log.d(Constants.TAG, "RxError:", e)
    }

    /**
     * QR code generator
     */
    private fun qrImageGenerator(address: String): Maybe<Bitmap?> {
        return Maybe.fromCallable {
            try {
                BarcodeEncoder().createBitmap(MultiFormatWriter()
                        .encode(address, BarcodeFormat.QR_CODE, 720, 720, null))
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}