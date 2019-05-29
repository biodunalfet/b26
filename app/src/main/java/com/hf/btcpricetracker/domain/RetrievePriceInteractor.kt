package com.hf.btcpricetracker.domain

import com.gojuno.koptional.toOptional
import com.hf.btcpricetracker.data.BtcPriceData
import com.hf.btcpricetracker.data.IBtcPriceRepository
import com.hf.btcpricetracker.domain.common.RetrievePriceUseCase
import com.petertackage.kotlinoptions.Option
import com.petertackage.kotlinoptions.filterNotNone
import com.petertackage.kotlinoptions.optionOf
import io.reactivex.Observable
import javax.inject.Inject


class RetrievePriceInteractor @Inject constructor(val repository: IBtcPriceRepository) :
    RetrievePriceUseCase<String, Pair<String, BtcPriceData>> {

    override fun getPriceStream(params: String): Observable<Pair<String, BtcPriceData>> {
        repository.fetchBtcPrice(params)

        return repository.getBtcPriceData(params)
            .filterNotNone()
    }
}