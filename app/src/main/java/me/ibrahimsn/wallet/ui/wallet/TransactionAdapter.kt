package me.ibrahimsn.wallet.ui.wallet

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.ibrahimsn.wallet.R
import me.ibrahimsn.wallet.entity.Transaction

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private var transactions = listOf<Transaction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_transaction, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.bind(transactions[pos])
    }

    fun setItems(transactions: List<Transaction>) {
        val diffCallback = DiffUtil.calculateDiff(MyDiffCallback(this.transactions, transactions))
        this.transactions = transactions
        diffCallback.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private var transaction: Transaction? = null

        fun bind(transaction: Transaction) {
            this.transaction = transaction
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