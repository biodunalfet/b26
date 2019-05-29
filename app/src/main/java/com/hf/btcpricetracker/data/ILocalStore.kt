package com.hf.btcpricetracker.data

interface ILocalStore {

    fun getLocalBtcPriceForTimeSpan(timeSpan: String): BtcPriceSchema?
    fun putBtcPriceData(timeSpan: String, btcPriceSchema: BtcPriceSchema)
    fun deletePriceData(timeSpan: String)

}