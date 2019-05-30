package me.ibrahimsn.wallet.util

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.ibrahimsn.wallet.entity.Wallet

object RxBus {

    private val publisher = PublishSubject.create<Any>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)

    class RxEvent {
        data class OnChangeCurrentWallet(val wallet: Wallet)
    }
}