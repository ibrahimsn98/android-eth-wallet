package me.ibrahimsn.wallet.repository

import android.text.TextUtils
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.ibrahimsn.wallet.entity.NetworkInfo
import me.ibrahimsn.wallet.entity.Wallet
import me.ibrahimsn.wallet.manager.GethAccountManager
import me.ibrahimsn.wallet.util.Constants.ETHEREUM_NETWORK_NAME
import me.ibrahimsn.wallet.util.Constants.ETH_SYMBOL
import me.ibrahimsn.wallet.util.Constants.KOVAN_NETWORK_NAME
import me.ibrahimsn.wallet.util.Constants.ROPSTEN_NETWORK_NAME
import me.ibrahimsn.wallet.util.RxBus
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Numeric
import java.math.BigInteger
import javax.inject.Inject

class EthereumNetworkRepository @Inject constructor(private val preferencesRepository: PreferencesRepository,
                                                    private val accountManager: GethAccountManager) {

    private val NETWORKS = arrayOf(NetworkInfo(ETHEREUM_NETWORK_NAME, ETH_SYMBOL,
            "https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk",
            "https://api.etherscan.io/api/",
            "https://etherscan.io/", 1, true),

            NetworkInfo(ROPSTEN_NETWORK_NAME, ETH_SYMBOL,
                    "https://ropsten.infura.io/llyrtzQ3YhkdESt2Fzrk",
                    "https://ropsten.etherscan.io/api/",
                    "https://ropsten.etherscan.io", 3, false),

            NetworkInfo(KOVAN_NETWORK_NAME, ETH_SYMBOL,
                    "https://kovan.infura.io/llyrtzQ3YhkdESt2Fzrk",
                    "https://kovan.etherscan.io/api/",
                    "https://kovan.etherscan.io", 42, false))

    private var defaultNetwork = getByName(preferencesRepository.getDefaultNetwork()) ?: NETWORKS[1]
    private var web3j = Web3j.build(HttpService(getDefaultNetwork().rpcServerUrl))

    fun setDefaultNetworkInfo(pos: Int) {
        defaultNetwork = NETWORKS[pos]
        preferencesRepository.setDefaultNetwork(defaultNetwork.name)
        RxBus.publish(RxBus.RxEvent.OnChangeDefaultNetwork(defaultNetwork))
    }

    fun getDefaultNetwork(): NetworkInfo {
        return defaultNetwork
    }

    private fun getByName(name: String?): NetworkInfo? {
        if (!TextUtils.isEmpty(name))
            for (NETWORK in NETWORKS)
                if (name == NETWORK.name)
                    return NETWORK
        return null
    }

    fun getWalletBalance(wallet: Wallet): Single<BigInteger> {
        return Single.fromCallable {
            web3j.ethGetBalance(wallet.address, DefaultBlockParameterName.LATEST).send().balance
        }
    }

    fun createTransaction(from: Wallet, toAddress: String, subUnitAmount: BigInteger,
                          gasPrice: BigInteger, gasLimit: Long, password: String): Single<String> {

        return Single.fromCallable<Long> {
            web3j.ethGetTransactionCount(from.address, DefaultBlockParameterName.LATEST).send().transactionCount.toLong()
        }.flatMap<ByteArray> {
            accountManager.signTransaction(from, password, toAddress, subUnitAmount, gasPrice, gasLimit,
                    it, getDefaultNetwork().chainId.toLong())
        }.flatMap {
            Single.fromCallable {
                val raw = web3j.ethSendRawTransaction(Numeric.toHexString(it)).send()

                if (raw.hasError())
                    throw Exception(raw.error.message)

                raw.transactionHash
            }
        }.subscribeOn(Schedulers.io())
    }
}