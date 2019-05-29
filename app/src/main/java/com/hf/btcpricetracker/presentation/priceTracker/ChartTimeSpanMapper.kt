package com.hf.btcpricetracker.presentation.priceTracker

import com.hf.btcpricetracker.presentation.priceTracker.TIMESPAN.*
import java.lang.IllegalStateException
import javax.inject.Inject

class ChartTimeSpanMapper @Inject constructor() : IChartTimeSpanMapper {

    override fun mapTimeSpanToParam(timeSpan: TIMESPAN): String {

        return when (timeSpan) {
            WEEK -> "1week"
            MONTH -> "1months"
            YEAR -> "1year"
            ALL -> "all"
            else -> throw IllegalStateException("Invalid timeSpan selected")
        }
    }
}