package com.hf.btcpricetracker.data

import io.reactivex.Single

interface IRemoteDataSource {
    fun getPriceFromNetwork(timeSpan: String): Single<BtcPriceSchema>
}