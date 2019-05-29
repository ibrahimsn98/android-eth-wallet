package me.ibrahimsn.wallet.ui.wallets

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_wallet.view.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.entity.Wallet

class WalletAdapter(private val callback: WalletCallback) : RecyclerView.Adapter<WalletAdapter.ViewHolder>() {

    var wallets = listOf<Wallet>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_wallet, parent,
                false), callback)
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

    class ViewHolder(private val view: View,
                     private val callback: WalletCallback) : RecyclerView.ViewHolder(view) {

        private var wallet: Wallet? = null

        init {
            view.setOnClickListener {
                if (wallet != null)
                    callback.onWalletClicked(wallet!!)
            }

            view.setOnLongClickListener {
                if (wallet != null)
                    callback.onMoreClicked(wallet!!)

                true
            }

            view.ibMore.setOnClickListener {
                if (wallet != null)
                    callback.onMoreClicked(wallet!!)
            }
        }

        fun bind(wallet: Wallet) {
            this.wallet = wallet
            view.tvAddress.text = wallet.address
            view.tvName.text = wallet.name
        }
    }

    interface WalletCallback {
        fun onWalletClicked(wallet: Wallet)
        fun onMoreClicked(wallet: Wallet)
    }
}