package me.ibrahimsn.wallet.ui.addWallet.importWallet

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import me.ibrahimsn.wallet.ui.addWallet.importWallet.importAddress.ImportAddressFragment
import me.ibrahimsn.wallet.ui.addWallet.importWallet.importKey.ImportKeyFragment

class ImportWalletAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(pos: Int): Fragment {
        return when (pos) {
            0 -> ImportKeyFragment()
            1 -> ImportAddressFragment()
            else -> ImportAddressFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }
}