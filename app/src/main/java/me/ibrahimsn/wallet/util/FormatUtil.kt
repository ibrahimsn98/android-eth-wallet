package me.ibrahimsn.wallet.util

import android.annotation.SuppressLint
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.*

object FormatUtil {

    /*fun valueToETH(value: String): Double {
        return (value.toBigInteger() / 10.toBigInteger().pow(18)).toDouble()
    }

    fun valueToETH(value: BigInteger): Double {
        return (value / 10.toBigInteger().pow(18)).toDouble()
    }*/

    @SuppressLint("SimpleDateFormat")
    fun timeStampToDate(timeStamp: String): String {
        val formatter = SimpleDateFormat("dd MMMM hh:mm")
        return formatter.format(Date(timeStamp.toLong()))
    }

}