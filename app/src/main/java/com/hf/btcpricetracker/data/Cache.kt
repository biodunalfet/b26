package com.hf.btcpricetracker.data

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ConcurrentHashMap

class Cache<Key, Value>(
    val timeStampProvider: ITimeStampProvider,
    val timeOutMs: Long
) : ICache<Key, Value> {

    private val cache = ConcurrentHashMap<Key, CacheEntry<Value>>()

    override fun putSingular(key: Key, value: Value) {
        val entry = CacheEntry(value, timeStampProvider.currentTimeInMs())
        cache[key] = entry
    }

    override fun putAll(data: List<Pair<Key, Value>>) {
        for (entry in data) {
            cache[entry.first] = CacheEntry(entry.second, timeStampProvider.currentTimeInMs())
        }
    }

    override fun getSingular(key: Key): Maybe<Pair<Key, Value>> {
        return Maybe.fromCallable {
            cache.containsKey(key)
        }.filter {
            it == true
        }.map {
            cache[key]!!
        }.filter {
            !isExpired(it)
        }.map {
            Pair(key, it!!.value)
        }.subscribeOn(Schedulers.computation())
    }

    override fun getAll(): Maybe<List<Pair<Key, Value>>> {
        return Observable.fromIterable(cache.keys)
            .filter {
                !isExpired(cache[it]!!)
            }
            .map {
                Pair(it, cache[it]!!.value)
            }
            .toList()
            .filter {
                it.size != 0
            }.subscribeOn(Schedulers.computation())
    }

    override fun deleteSingular(key: Key) {
        if (cache.containsKey(key)) {
            cache.remove(key)
        }
    }

    override fun deleteAll() {
        cache.clear()
    }

    private fun isExpired(cacheEntry: CacheEntry<Value>): Boolean {
        val currentTime = timeStampProvider.currentTimeInMs()
        val dateCreated = cacheEntry.dateCreated
        val timeDiff = currentTime - dateCreated
        return timeDiff > timeOutMs
    }
}

interface ICache<Key, Value> {
    fun putSingular(key: Key, value: Value)
    fun putAll(data: List<Pair<Key, Value>>)
    fun getSingular(key: Key): Maybe<Pair<Key, Value>>
    fun getAll(): Maybe<List<Pair<Key, Value>>>
    fun deleteSingular(key: Key)
    fun deleteAll()
}


