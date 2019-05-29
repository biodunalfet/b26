package com.hf.btcpricetracker.presentation.priceTracker

interface IChartTimeSpanMapper {

    fun mapTimeSpanToParam(timeSpan: TIMESPAN): String

}