package com.hf.btcpricetracker.data

data class CacheEntry<Value>(
    val value: Value,
    val dateCreated: Long
)

