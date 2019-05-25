package me.ibrahimsn.wallet.repository

import android.content.Context
import android.preference.PreferenceManager
import me.ibrahimsn.wallet.entity.GasSettings
import me.ibrahimsn.wallet.util.Constants
import java.math.BigInteger
import javax.inject.Inject

class PreferencesRepository @Inject constructor(context: Context) {

    private val CURRENT_ACCOUNT_ADDRESS_KEY = "current_account_address"
    private val DEFAULT_NETWORK_NAME_KEY = "default_network_name"
    private val GAS_PRICE_KEY = "gas_price"
    private val GAS_LIMIT_KEY = "gas_limit"
    private val GAS_LIMIT_FOR_TOKENS_KEY = "gas_limit_for_tokens"

    private var prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun getCurrentWalletAddress(): String? {
        return prefs.getString(CURRENT_ACCOUNT_ADDRESS_KEY, null)
    }

    fun setCurrentWalletAddress(address: String) {
        prefs.edit().putString(CURRENT_ACCOUNT_ADDRESS_KEY, address).apply()
    }

    fun getDefaultNetwork(): String? {
        return prefs.getString(DEFAULT_NETWORK_NAME_KEY, null)
    }

    fun setDefaultNetwork(netName: String) {
        prefs.edit().putString(DEFAULT_NETWORK_NAME_KEY, netName).apply()
    }

    fun getGasSettings(forTokenTransfer: Boolean): GasSettings {
        val gasPrice = BigInteger(prefs.getString(GAS_PRICE_KEY, Constants.DEFAULT_GAS_PRICE))
        var gasLimit = BigInteger(prefs.getString(GAS_LIMIT_KEY, Constants.DEFAULT_GAS_LIMIT))

        if (forTokenTransfer)
            gasLimit = BigInteger(prefs.getString(GAS_LIMIT_FOR_TOKENS_KEY,
                    Constants.DEFAULT_GAS_LIMIT_FOR_TOKENS))

        return GasSettings(gasPrice, gasLimit)
    }

    fun setGasSettings(gasSettings: GasSettings) {
        prefs.edit().putString(GAS_PRICE_KEY, gasSettings.gasPrice.toString()).apply()
        prefs.edit().putString(GAS_PRICE_KEY, gasSettings.gasLimit.toString()).apply()
    }
}