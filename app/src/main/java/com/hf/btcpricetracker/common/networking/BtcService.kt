package com.hf.btcpricetracker.common.networking

import com.hf.btcpricetracker.data.BtcPriceSchema
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface BtcService {

    @GET("/charts/market-price")
    fun getPriceChartData(@Query("timeSpan") timeSpan: String): Single<BtcPriceSchema>

}