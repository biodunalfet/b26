package com.hf.btcpricetracker.data

class TimeStampProvider : ITimeStampProvider {
    override fun currentTimeInMs(): Long {
        return System.currentTimeMillis()
    }
}

interface ITimeStampProvider {
    fun currentTimeInMs(): Long
}