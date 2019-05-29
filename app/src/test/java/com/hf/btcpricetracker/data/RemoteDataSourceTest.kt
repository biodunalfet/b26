package com.hf.btcpricetracker.data

import android.accounts.NetworkErrorException
import com.hf.btcpricetracker.common.RxSchedulerOverrideRule
import com.hf.btcpricetracker.common.networking.BtcService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observer
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.lang.IllegalStateException

class RemoteDataSourceTest {

    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulerOverrideRule()

    lateinit var SUT: RemoteDataSource
    lateinit var apiService: BtcService


    @Before
    fun setUp() {
        apiService = mock()
        SUT = RemoteDataSource(apiService)
    }

    @Test
    fun fetchBtcPrice_returnSingle() {

        val data = BtcPriceSchema()
        whenever(apiService.getPriceChartData(any())).thenReturn(Single.just(data))
        val result = SUT.getPriceFromNetwork("week")

        result.test().assertValue {
            it == data
        }
    }

//    @Test
//    fun fetchPrice_throwsError() {
//
//        whenever(apiService.getPriceChartData(any())).thenReturn(Single
//            .error(HttpException
//                (Response.error<ErrorBody>(403,
//                ResponseBody.create(MediaType.parse("application/json"), "{}")))))
//
//        val result = SUT.getPriceFromNetwork("week")
//
//        argumentCaptor<Throwable>().apply {
//            result.doOnError {
//                capture()
//            }
//            assertThat(allValues[0], `is`(instanceOf(HttpException::class.java)))
//        }
//    }
//
//    data class ErrorBody(val message : String)
}