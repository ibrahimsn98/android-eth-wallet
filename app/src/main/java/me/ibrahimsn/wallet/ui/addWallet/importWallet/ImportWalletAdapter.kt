package me.ibrahimsn.wallet.ui.addWallet.importWallet

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import me.ibrahimsn.wallet.ui.addWallet.importWallet.importAddress.ImportAddressFragment
import me.ibrahimsn.wallet.ui.addWallet.importWallet.importKey.ImportKeyFragment
import me.ibrahimsn.wallet.ui.addWallet.importWallet.importKeystore.ImportKeystoreFragment

class ImportWalletAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(pos: Int): Fragment {
        return when (pos) {
            0 -> ImportKeystoreFragment()
            1 -> ImportKeyFragment()
            2 -> ImportAddressFragment()
            else -> ImportKeystoreFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }
}