package me.ibrahimsn.wallet.ui.wallets

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.row_wallet.view.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.entity.Wallet

class WalletAdapter(private val callback: WalletCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var wallets = listOf<Wallet>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0)
        return ViewHolderTitle(LayoutInflater.from(parent.context).inflate(R.layout.row_wallet_title, parent,
                false))

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_wallet, parent,
                false), callback)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        if (holder is ViewHolderTitle)
                holder.bind(pos)
        else if (holder is ViewHolder) {
            if (pos <= getWalletCount())
                holder.bind(wallets[pos-1])
            else
                holder.bind(wallets[pos-2])
        }
    }

    private fun getWalletCount(): Int {
        var count = 0
        wallets.forEach {
            if (it.isWallet) count++
        }
        return count
    }

    fun setItems(wallets: List<Wallet>) {
        val diffCallback = DiffUtil.calculateDiff(MyDiffCallback(this.wallets, wallets))
        this.wallets = wallets
        diffCallback.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0
            getWalletCount() + 1 -> 0
            else -> 1
        }
    }

    override fun getItemCount(): Int {
        return wallets.size + 2
    }

    class ViewHolderTitle(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(pos: Int) {
            view.tvTitle.text = if (pos == 0)
                 "Wallets"
            else
                "Public Addresses"
        }
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

    inner class MyDiffCallback(private val oldItems: List<Wallet>,
                               private val newItems: List<Wallet>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldItems[oldPos].id == newItems[newPos].id
        }

        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldItems[oldPos] == newItems[newPos]
        }
    }
}