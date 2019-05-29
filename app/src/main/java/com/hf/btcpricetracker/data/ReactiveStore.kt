package com.hf.btcpricetracker.data

import com.petertackage.kotlinoptions.None
import com.petertackage.kotlinoptions.Option
import com.petertackage.kotlinoptions.optionOf
import com.petertackage.kotlinoptions.orDefault
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.*

class ReactiveStore<Key, Value : Any>(val cache: ICache<Key, Value>) : IReactiveStore<Key, Value> {

    private val allSubject: Subject<Option<List<Value>>> by lazy {
        PublishSubject.create<Option<List<Value>>>().toSerialized()
    }

    private val subjectMap = HashMap<Key, Subject<Option<Pair<Key, Value>>>>()

    override fun storeSingular(key: Key, value: Value) {
        cache.putSingular(key, value)
        getOrCreateSubjectForKey(key).onNext(optionOf(key to value))
        allSubject.onNext(getAllValues())
    }

    override fun storeAll(data: List<Pair<Key, Value>>) {
        cache.putAll(data)

        for (d in data) {
            getOrCreateSubjectForKey(d.first).onNext(optionOf(d))
        }

        allSubject.onNext(getAllValues())
    }

    override fun replaceAll(data: List<Pair<Key, Value>>) {
        cache.deleteAll()
        storeAll(data)
    }

    override fun getSingular(key: Key): Observable<Option<Pair<Key, Value>>> {
        return Observable.defer { getOrCreateSubjectForKey(key).startWith(getValue(key)) }
            .observeOn(Schedulers.computation())
    }

    private fun getOrCreateSubjectForKey(key: Key): Subject<Option<Pair<Key, Value>>> {
        synchronized(subjectMap) {
            return optionOf(subjectMap[key]).orDefault { createAndStoreNewSubjectForKey(key) }
        }
    }

    private fun createAndStoreNewSubjectForKey(key: Key): Subject<Option<Pair<Key, Value>>> {
        val processor = PublishSubject.create<Option<Pair<Key, Value>>>().toSerialized()
        synchronized(subjectMap) {
            subjectMap.put(key, processor)
        }
        return processor
    }

    override fun getAll(): Observable<Option<List<Value>>> {

        return Observable.defer {
            allSubject.startWith(getAllValues())
        }.observeOn(Schedulers.computation())
    }

    private fun getValue(key: Key): Option<Pair<Key, Value>> {
        return cache.getSingular(key).map {
            optionOf(it)
        }.blockingGet(optionOf(None).getUnsafe())
    }

    private fun getAllValues(): Option<List<Value>> {
        return cache.getAll()
            .map {
                it.map { item ->
                    item.second
                }
            }.map {
                optionOf(it)
            }.blockingGet(optionOf(None).getUnsafe())
    }
}

interface IReactiveStore<Key, Value : Any> {
    fun storeSingular(key: Key, value: Value)
    fun storeAll(data: List<Pair<Key, Value>>)
    fun replaceAll(data: List<Pair<Key, Value>>)
    fun getSingular(key: Key): Observable<Option<Pair<Key, Value>>>
    fun getAll(): Observable<Option<List<Value>>>
}