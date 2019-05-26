package me.ibrahimsn.wallet.ui.wallet

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_wallet.view.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.entity.Wallet

class WalletAdapter : RecyclerView.Adapter<WalletAdapter.ViewHolder>() {

    var wallets = listOf<Wallet>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_wallet, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.bind(wallets[pos])
    }

    fun setItems(wallets: List<Wallet>) {
        this.wallets = wallets
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return wallets.size
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private var wallet: Wallet? = null

        fun bind(wallet: Wallet) {
            this.wallet = wallet
            view.tvAddress.text = wallet.address
        }
    }
}