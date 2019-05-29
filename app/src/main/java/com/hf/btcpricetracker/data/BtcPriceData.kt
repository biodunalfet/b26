package com.hf.btcpricetracker.data

data class BtcPriceData(
    val period: String? = "",
    val unit: String? = "",
    val values: List<BtcPriceDataPoint>? = emptyList()
)