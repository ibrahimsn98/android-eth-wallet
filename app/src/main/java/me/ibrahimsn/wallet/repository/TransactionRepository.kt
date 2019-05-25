package me.ibrahimsn.wallet.repository

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.EtherScanResponse
import me.ibrahimsn.wallet.entity.Transaction
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.manager.GethAccountManager
import me.ibrahimsn.wallet.manager.TransactionManager
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import java.lang.Exception
import java.math.BigInteger
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val networkRepository: EthereumNetworkRepository,
                                                private val accountManager: GethAccountManager,
                                                private val transactionManager: TransactionManager) {

    fun fetchTransaction(address: String): Observable<EtherScanResponse> {
        return transactionManager.fetchTransaction(address)
    }

    fun findTransaction(wallet: Wallet, transactionHash: String): Maybe<Transaction> {
        return fetchTransaction(wallet.address).firstElement().flatMap {
            for (transaction in it.result)
                if (transaction.hash == transactionHash)
                    Maybe.just(transaction)
            null
        }
    }

    fun createTransaction(from: Wallet, toAddress: String, subUnitAmount: BigInteger,
                          gasPrice: BigInteger, gasLimit: Long,
                          data: ByteArray, password: String): Single<String> {

        val web3j = Web3jFactory.build(HttpService(networkRepository.getDefaultNetwork().rpcServerUrl))
        return Single.fromCallable<Long> {
            web3j.ethGetTransactionCount(from.address, DefaultBlockParameterName.LATEST)
                    .send().transactionCount.toLong()
        }.flatMap<ByteArray> {
            accountManager.signTransaction(from, password, toAddress, subUnitAmount, gasPrice, gasLimit,
                    it, data, networkRepository.getDefaultNetwork().chainId.toLong())
        }.flatMap {
            Single.fromCallable {
                val raw = web3j.ethSendRawTransaction(String.format("%02X", it)).send()

                if (raw.hasError())
                    throw Exception(raw.error.message)

                raw.transactionHash
            }
        }.subscribeOn(Schedulers.io())
    }
}