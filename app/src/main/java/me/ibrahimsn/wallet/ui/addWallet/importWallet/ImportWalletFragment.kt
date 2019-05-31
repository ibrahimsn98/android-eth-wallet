package me.ibrahimsn.wallet.ui.addWallet.importWallet

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import kotlinx.android.synthetic.main.fragment_import_wallet.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.base.BaseFragment
import me.ibrahimsn.wallet.ui.addWallet.AddWalletActivity

class ImportWalletFragment : BaseFragment<AddWalletActivity>() {

    override fun layoutRes(): Int {
        return R.layout.fragment_import_wallet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ImportWalletAdapter(childFragmentManager)
        viewPager.adapter = adapter

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }
        })

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                tabLayout.setScrollPosition(p0, p1, false)
            }

            override fun onPageSelected(pos: Int) {
                tabLayout.getTabAt(pos)!!.select()
            }
        })
    }
}