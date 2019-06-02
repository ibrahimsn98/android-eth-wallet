package me.ibrahimsn.wallet.util

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

object BalanceUtil {

    private const val ETHER_DECIMALS = 18

    fun ethToUsd(priceUsd: String, ethBalance: BigInteger): String {
        val usd = subunitToBase(ethBalance).multiply(BigDecimal(priceUsd))
        return usd.setScale(2, RoundingMode.CEILING).toString()
    }

    /**
     * Base - taken to mean default unit for a currency e.g. ETH, DOLLARS
     * Subunit - taken to mean subdivision of base e.g. WEI, CENTS
     *
     * @param baseAmountStr - decimal amonut in base unit of a given currency
     * @return amount in subunits
     */
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

    /**
     * @param subunitAmount - amount in subunits
     * @return amount in base units
     */
    fun subunitToBase(subunitAmount: BigInteger): BigDecimal {
        return BigDecimal(subunitAmount).divide(BigDecimal.valueOf(10).pow(ETHER_DECIMALS))
    }
}