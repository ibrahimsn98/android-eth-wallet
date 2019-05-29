package me.ibrahimsn.wallet.ui.wallet

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_transaction_sent.view.*
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.entity.Transaction
import me.ibrahimsn.wallet.util.FormatUtil

class TransactionAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var address: String? = null
    private var transactions = listOf<Transaction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0)
            ViewHolderSent(LayoutInflater.from(parent.context).inflate(R.layout.row_transaction_sent,
                    parent, false))
        else
            ViewHolderReceived(LayoutInflater.from(parent.context).inflate(R.layout.row_transaction_sent,
                    parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        if (holder is ViewHolderSent)
            holder.bind(transactions[pos])
        else if (holder is ViewHolderReceived)
            holder.bind(transactions[pos])
    }

    fun setWalletAddress(address: String) {
        this.address = address
        notifyDataSetChanged()
    }

    fun setItems(transactions: List<Transaction>) {
        val diffCallback = DiffUtil.calculateDiff(MyDiffCallback(this.transactions, transactions))
        this.transactions = transactions
        diffCallback.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return if (address == transactions[position].from)
            0
        else
            1
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    inner class ViewHolderSent(private val view: View) : RecyclerView.ViewHolder(view) {

        private var transaction: Transaction? = null

        fun bind(transaction: Transaction) {
            this.transaction = transaction

            view.tvAmount.text = String.format(context.getText(R.string.eth_sent_amount).toString(),
                    FormatUtil.valueToETH(transaction.value).toString())
            view.tvDate.text = FormatUtil.timeStampToDate(transaction.timeStamp)
        }
    }

    inner class ViewHolderReceived(private val view: View) : RecyclerView.ViewHolder(view) {

        private var transaction: Transaction? = null

        fun bind(transaction: Transaction) {
            this.transaction = transaction

            view.tvAmount.text = String.format(context.getText(R.string.eth_received_amount).toString(),
                    FormatUtil.valueToETH(transaction.value).toString())
            view.tvDate.text = FormatUtil.timeStampToDate(transaction.timeStamp)
        }
    }

    inner class MyDiffCallback(private val oldItems: List<Transaction>,
                               private val newItems: List<Transaction>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldItems[oldPos].hash == newItems[newPos].hash
        }

        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldItems[oldPos] == newItems[newPos]
        }
    }
}