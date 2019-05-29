package com.hf.btcpricetracker.data

data class BtcPriceSchema(
    val unit: String? = "",
    val period: String? = "",
    val description: String? = "",
    val values: List<BtcPriceDataPoint>? = emptyList()
)

data class BtcPriceDataPoint(val x: String, val y: String)