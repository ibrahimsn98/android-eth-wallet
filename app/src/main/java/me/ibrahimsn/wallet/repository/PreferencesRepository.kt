package me.ibrahimsn.wallet.repository

import android.content.Context
import android.preference.PreferenceManager
import me.ibrahimsn.wallet.entity.GasSettings
import me.ibrahimsn.wallet.util.Constants
import java.math.BigInteger
import javax.inject.Inject

class PreferencesRepository @Inject constructor(context: Context) {

    private val DEFAULT_NETWORK_NAME_KEY = "default_network_name"
    private val GAS_PRICE_KEY = "gas_price"
    private val GAS_LIMIT_KEY = "gas_limit"

    private var prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun getCurrentWalletAddress(): String {
        return prefs.getString("current_account_address", "")!!
    }

    fun setCurrentWalletAddress(address: String) {
        prefs.edit().putString("current_account_address", address).apply()
    }

    fun getDefaultNetwork(): String? {
        return prefs.getString(DEFAULT_NETWORK_NAME_KEY, null)
    }

    fun setDefaultNetwork(netName: String) {
        prefs.edit().putString(DEFAULT_NETWORK_NAME_KEY, netName).apply()
    }

    fun setLastEthPriceUsd(price: String) {
        prefs.edit().putString("eth_price_in_usd", price).apply()
    }

    fun getLastEthPriceUsd(): String {
        return prefs.getString("eth_price_in_usd", "0")!!
    }

    fun getGasSettings(): GasSettings {
        val gasPrice = BigInteger(prefs.getString(GAS_PRICE_KEY, Constants.DEFAULT_GAS_PRICE))
        val gasLimit = prefs.getString(GAS_LIMIT_KEY, Constants.DEFAULT_GAS_LIMIT)!!.toLong()

        return GasSettings(gasPrice, gasLimit)
    }

    fun setGasSettings(gasSettings: GasSettings) {
        prefs.edit().putString(GAS_PRICE_KEY, gasSettings.gasPrice.toString()).apply()
        prefs.edit().putString(GAS_PRICE_KEY, gasSettings.gasLimit.toString()).apply()
    }
}