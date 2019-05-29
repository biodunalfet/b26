package com.hf.btcpricetracker.presentation.priceTracker

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TimestampConverter : ITimestampConverter {

    override fun convertToFormattedDate(date: String): String {

        val dateAsLong = date.toLongOrNull()

        dateAsLong?.let {
            val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            return sdf.format(dateAsLong * 1000)
        } ?: run {
            return "N/A"
        }
    }
}

interface ITimestampConverter {
    fun convertToFormattedDate(date: String): String
}