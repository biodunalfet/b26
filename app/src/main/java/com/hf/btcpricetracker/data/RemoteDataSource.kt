package com.hf.btcpricetracker.data

import com.hf.btcpricetracker.common.networking.BtcService
import io.reactivex.Single

class RemoteDataSource(val api: BtcService) : IRemoteDataSource {

    override fun getPriceFromNetwork(timeSpan: String): Single<BtcPriceSchema> {
        return api.getPriceChartData(timeSpan)
    }
}