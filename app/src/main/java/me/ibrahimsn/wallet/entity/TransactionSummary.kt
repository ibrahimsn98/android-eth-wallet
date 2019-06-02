package me.ibrahimsn.wallet.entity

import java.math.BigDecimal
import java.math.BigInteger

data class TransactionSummary (val from: Wallet,
                               val to: String,
                               val amount: BigInteger,
                               val networkFee: BigDecimal)