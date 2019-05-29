package com.hf.btcpricetracker.domain.common

import io.reactivex.Observable
import java.util.*

interface RetrievePriceUseCase<Param, Result> {

    fun getPriceStream(params: String): Observable<Result>

}