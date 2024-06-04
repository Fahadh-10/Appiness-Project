package com.example.appinessproject.managers

import com.example.appiness.model.Product
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import kotlin.math.roundToInt

object Utils {

    /**
     * Converts a numeric amount into a formatted currency string.
     *
     * @param amount The numeric amount to be formatted.
     * @return A string representing the numeric amount in currency format, e.g., "$1,234.56".
     */
    fun toCurrencyString(amount: Double?): String {
        if (amount == null) return ""
        val local = Locale("en", "IN")
        val numberFormat = NumberFormat.getCurrencyInstance(local)
        return try {
            val currency = Currency.getInstance("INR")
            numberFormat.currency = currency
            val formattedString = numberFormat.format(amount)
            formattedString.replace("\\.\\d+".toRegex(), "")
        } catch (e: IllegalArgumentException) {
            val formattedString = numberFormat.format(amount)
            formattedString.replace("\\.\\d+".toRegex(), "")
        }
    }

    /**
     * Calculates the discount percentage for a given product.
     * @return The discount percentage as a string.
     */
    fun calculateDiscountPercentage(product: Product?): String {
        val realPrice = product?.price?.toDouble()
        val discountAmount = product?.discount?.toDouble()
        val oldPrice = realPrice?.plus(discountAmount?: 0.0)
        val discountPercentage = (discountAmount?.div(oldPrice?: 0.0))?.times(100)
        val roundedDiscountPercentage = if ((discountPercentage ?: 0.0) > 0) discountPercentage?.roundToInt() else 0
        return roundedDiscountPercentage.toString().plus("%")
    }
}