package com.hf.btcpricetracker.presentation.priceTracker

import java.text.NumberFormat
import java.util.*

class MoneyFormatter : IMoneyFormatter {

    override fun formatMoney(amount: String): String {
        val amountAsDouble = amount.toDoubleOrNull()

        amountAsDouble?.let {
            val format = NumberFormat.getCurrencyInstance(Locale.US)
            return format.format(amountAsDouble)
        } ?: run {
            return "N/A"
        }
    }
}

interface IMoneyFormatter {
    fun formatMoney(amount: String): String
}