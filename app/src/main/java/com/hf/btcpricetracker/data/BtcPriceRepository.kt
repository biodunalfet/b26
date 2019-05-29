package com.hf.btcpricetracker.data

import android.util.Log
import com.petertackage.kotlinoptions.Option
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class BtcPriceRepository(
    private val localStore: IReactiveStore<String, BtcPriceData>,
    private val remoteDataSource: IRemoteDataSource,
    private val btcPriceSchemaMapper: IBtcSchemaMapper
) : IBtcPriceRepository {


    override fun getBtcPriceData(timeSpan: String): Observable<Option<Pair<String, BtcPriceData>>> {
        return localStore.getSingular(timeSpan)
    }

    override fun fetchBtcPrice(timeSpan: String): Completable {

        remoteDataSource.getPriceFromNetwork(timeSpan)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { btcPriceSchemaMapper.apply(it) }
            .subscribe({ success ->
                success?.let {
                    localStore.storeSingular(timeSpan, it)
                }
            }, { error ->
                Log.d("networkError", error.message)
            })


        return Completable.complete()
    }
}


interface IBtcPriceRepository {
    fun getBtcPriceData(timeSpan: String): Observable<Option<Pair<String, BtcPriceData>>>
    fun fetchBtcPrice(timeSpan: String): Completable
}