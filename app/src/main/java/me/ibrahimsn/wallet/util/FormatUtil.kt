package me.ibrahimsn.wallet.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object FormatUtil {

    @SuppressLint("SimpleDateFormat")
    fun timeStampToDate(timeStamp: String): String {
        val formatter = SimpleDateFormat("dd MMMM hh:mm")
        return formatter.format(Date(timeStamp.toLong()))
    }
}