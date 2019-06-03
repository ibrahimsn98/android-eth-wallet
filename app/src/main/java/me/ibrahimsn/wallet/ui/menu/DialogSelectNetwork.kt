package me.ibrahimsn.wallet.ui.menu

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_select_network.*
import me.ibrahimsn.wallet.R

class DialogSelectNetwork : BottomSheetDialogFragment() {

    private var listener: OnNetworkSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_select_network, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lyEthereum.setOnClickListener {
            Toast.makeText(activity, R.string.coming_soon, Toast.LENGTH_SHORT).show()
            //if (listener != null)
            //    listener!!.onNetworkSelected(0)
            //dismiss()
        }

        lyRopsten.setOnClickListener {
            if (listener != null)
                listener!!.onNetworkSelected(1)
            dismiss()
        }

        lyKovan.setOnClickListener {
            if (listener != null)
                listener!!.onNetworkSelected(2)
            dismiss()
        }
    }

    fun setOnNetworkSelectedListener(onNetworkSelectedListener: OnNetworkSelectedListener) {
        this.listener = onNetworkSelectedListener
    }

    interface OnNetworkSelectedListener {
        fun onNetworkSelected(pos: Int)
    }
}