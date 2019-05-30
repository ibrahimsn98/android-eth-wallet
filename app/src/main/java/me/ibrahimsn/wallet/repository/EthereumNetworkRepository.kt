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
import me.ibrahimsn.wallet.util.FormatUtil
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import java.lang.Exception
import java.math.BigInteger
import java.util.HashSet
import javax.inject.Inject

class EthereumNetworkRepository @Inject constructor(private val preferencesRepository: PreferencesRepository,
                                                    private val accountManager: GethAccountManager) {

    private val NETWORKS = arrayOf(NetworkInfo(ETHEREUM_NETWORK_NAME, ETH_SYMBOL,
            "https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk",
            "https://api.etherscan.io/api/",
            "https://etherscan.io/", 1, true),

            NetworkInfo(KOVAN_NETWORK_NAME, ETH_SYMBOL,
                    "https://kovan.infura.io/llyrtzQ3YhkdESt2Fzrk",
                    "https://kovan.etherscan.io/api/",
                    "https://kovan.etherscan.io", 42, false),

            NetworkInfo(ROPSTEN_NETWORK_NAME, ETH_SYMBOL,
                    "https://ropsten.infura.io/llyrtzQ3YhkdESt2Fzrk",
                    "https://ropsten.etherscan.io/api/",
                    "https://ropsten.etherscan.io", 3, false))

    private var defaultNetwork = getByName(preferencesRepository.getDefaultNetwork()) ?: NETWORKS[2]
    private var web3j = Web3j.build(HttpService(getDefaultNetwork().rpcServerUrl))

    fun getAvailableNetworkList(): Array<NetworkInfo> {
        return NETWORKS
    }

    fun setDefaultNetworkInfo(networkInfo: NetworkInfo) {
        defaultNetwork = networkInfo
        preferencesRepository.setDefaultNetwork(defaultNetwork.name)
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

    fun getWalletBalance(wallet: Wallet): Single<Double> {
        return Single.fromCallable {
            FormatUtil.valueToETH(web3j.ethGetBalance(wallet.address, DefaultBlockParameterName.LATEST).send().balance)
        }
    }

    fun calculateFee() {

    }

    fun createTransaction(from: Wallet, toAddress: String, subUnitAmount: BigInteger,
                          gasPrice: BigInteger, gasLimit: Long,
                          data: ByteArray, password: String): Single<String> {

        return Single.fromCallable<Long> {
            web3j.ethGetTransactionCount(from.address, DefaultBlockParameterName.LATEST)
                    .send().transactionCount.toLong()
        }.flatMap<ByteArray> {
            accountManager.signTransaction(from, password, toAddress, subUnitAmount, gasPrice, gasLimit,
                    it, data, getDefaultNetwork().chainId.toLong())
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