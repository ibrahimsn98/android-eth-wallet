package me.ibrahimsn.wallet.repository

import android.content.Context
import android.preference.PreferenceManager
import javax.inject.Inject

class PreferencesRepository @Inject constructor(context: Context) {

    private var prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun getCurrentWalletAddress(): String {
        return prefs.getString("current_account_address", "")!!
    }

    fun setCurrentWalletAddress(address: String) {
        prefs.edit().putString("current_account_address", address).apply()
    }

    fun getDefaultNetwork(): String? {
        return prefs.getString("default_network_name", null)
    }

    fun setDefaultNetwork(netName: String) {
        prefs.edit().putString("default_network_name", netName).apply()
    }
}