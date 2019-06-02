package me.ibrahimsn.wallet.util

import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

object BalanceUtil {

    private const val ETHER_DECIMALS = 18
    private const val weiInEth = "1000000000000000000"

    fun weiToEth(wei: BigInteger): BigDecimal {
        return Convert.fromWei(BigDecimal(wei), Convert.Unit.ETHER)
    }

    fun weiToEth(wei: BigInteger, sigFig: Int): String {
        val eth = weiToEth(wei)
        val scale = sigFig - eth.precision() + eth.scale()
        val eth_scaled = eth.setScale(scale, RoundingMode.HALF_UP)
        return eth_scaled.toString()
    }

    fun EthToWei(eth: String): String {
        val wei = BigDecimal(eth).multiply(BigDecimal(weiInEth))
        return wei.toBigInteger().toString()
    }

    fun weiToGweiBI(wei: BigInteger): BigDecimal {
        return Convert.fromWei(BigDecimal(wei), Convert.Unit.GWEI)
    }

    fun weiToGwei(wei: BigInteger): String {
        return Convert.fromWei(BigDecimal(wei), Convert.Unit.GWEI).toPlainString()
    }

    fun gweiToWei(gwei: BigDecimal): BigInteger {
        return Convert.toWei(gwei, Convert.Unit.GWEI).toBigInteger()
    }

    fun ethToUsd(priceUsd: String, ethBalance: BigInteger): String {
        val usd = subunitToBase(ethBalance).multiply(BigDecimal(priceUsd))
        return usd.setScale(2, RoundingMode.CEILING).toString()
    }

    fun baseToSubunit(baseAmountStr: String): BigInteger {
        val baseAmount = BigDecimal(baseAmountStr)
        val subunitAmount = baseAmount.multiply(BigDecimal.valueOf(10).pow(ETHER_DECIMALS))
        return try {
            subunitAmount.toBigIntegerExact()
        } catch (ex: ArithmeticException) {
            assert(false)
            subunitAmount.toBigInteger()
        }
    }

    fun subunitToBase(subunitAmount: BigInteger): BigDecimal {
        return BigDecimal(subunitAmount)
                .divide(BigDecimal.valueOf(10)
                        .pow(ETHER_DECIMALS))
                .setScale(5, RoundingMode.DOWN)
    }
}