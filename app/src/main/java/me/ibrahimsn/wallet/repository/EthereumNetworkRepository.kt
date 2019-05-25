package me.ibrahimsn.wallet.repository

import android.text.TextUtils
import me.ibrahimsn.wallet.entity.NetworkInfo
import me.ibrahimsn.wallet.util.Constants.CLASSIC_NETWORK_NAME
import me.ibrahimsn.wallet.util.Constants.ETC_SYMBOL
import me.ibrahimsn.wallet.util.Constants.ETHEREUM_NETWORK_NAME
import me.ibrahimsn.wallet.util.Constants.ETH_SYMBOL
import me.ibrahimsn.wallet.util.Constants.KOVAN_NETWORK_NAME
import me.ibrahimsn.wallet.util.Constants.POA_NETWORK_NAME
import me.ibrahimsn.wallet.util.Constants.POA_SYMBOL
import me.ibrahimsn.wallet.util.Constants.ROPSTEN_NETWORK_NAME
import java.util.HashSet

class EthereumNetworkRepository(private val preferenceRepository: PreferenceRepository) {

    private val NETWORKS = arrayOf(NetworkInfo(ETHEREUM_NETWORK_NAME, ETH_SYMBOL,
            "https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk",
            "https://api.trustwalletapp.com/",
            "https://etherscan.io/", 1, true), NetworkInfo(CLASSIC_NETWORK_NAME, ETC_SYMBOL,
            "https://mewapi.epool.io/",
            "https://classic.trustwalletapp.com",
            "https://gastracker.io", 61, true), NetworkInfo(POA_NETWORK_NAME, POA_SYMBOL,
            "https://core.poa.network",
            "https://poa.trustwalletapp.com", "poa", 99, false), NetworkInfo(KOVAN_NETWORK_NAME, ETH_SYMBOL,
            "https://kovan.infura.io/llyrtzQ3YhkdESt2Fzrk",
            "https://kovan.trustwalletapp.com/",
            "https://kovan.etherscan.io", 42, false), NetworkInfo(ROPSTEN_NETWORK_NAME, ETH_SYMBOL,
            "https://ropsten.infura.io/llyrtzQ3YhkdESt2Fzrk",
            "https://ropsten.trustwalletapp.com/",
            "https://ropsten.etherscan.io", 3, false))

    private var defaultNetwork = getByName(preferenceRepository.getDefaultNetwork()) ?: NETWORKS[0]
    private val onNetworkChangedListeners = HashSet<OnNetworkChangeListener>()

    fun getAvailableNetworkList(): Array<NetworkInfo> {
        return NETWORKS
    }

    fun setDefaultNetworkInfo(networkInfo: NetworkInfo) {
        defaultNetwork = networkInfo
        preferenceRepository.setDefaultNetwork(defaultNetwork!!.name)

        for (listener in onNetworkChangedListeners)
            listener.onNetworkChanged(networkInfo)
    }

    fun getDefaultNetwork(): NetworkInfo {
        return defaultNetwork
    }

    private fun getByName(name: String): NetworkInfo? {
        if (!TextUtils.isEmpty(name))
            for (NETWORK in NETWORKS)
                if (name == NETWORK.name)
                    return NETWORK
        return null
    }

    fun addOnChangeDefaultNetwork(onNetworkChanged: OnNetworkChangeListener) {
        onNetworkChangedListeners.add(onNetworkChanged)
    }

    interface OnNetworkChangeListener {
        fun onNetworkChanged(networkInfo: NetworkInfo)
    }
}