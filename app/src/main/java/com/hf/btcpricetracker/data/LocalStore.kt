package com.hf.btcpricetracker.data

import java.util.concurrent.ConcurrentHashMap

class LocalStore : ILocalStore {

    private val concurrentHashMap = ConcurrentHashMap<String, BtcPriceSchema>()

    override fun putBtcPriceData(timeSpan: String, btcPriceSchema: BtcPriceSchema) {
        concurrentHashMap[timeSpan] = btcPriceSchema
    }

    override fun deletePriceData(timeSpan: String) {
        if (concurrentHashMap.containsKey(timeSpan)) {
            concurrentHashMap.remove(timeSpan)
        }
    }

    override fun getLocalBtcPriceForTimeSpan(timeSpan: String): BtcPriceSchema? {
        return if (concurrentHashMap.containsKey(timeSpan)) {
            concurrentHashMap[timeSpan]
        } else null
    }
}