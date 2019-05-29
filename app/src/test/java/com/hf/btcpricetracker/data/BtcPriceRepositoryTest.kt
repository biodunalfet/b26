package com.hf.btcpricetracker.data

import com.hf.btcpricetracker.common.RxSchedulerOverrideRule
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BtcPriceRepositoryTest {

    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulerOverrideRule()

    private val TIME_SPAN = "1week"

    lateinit var SUT: BtcPriceRepository

    lateinit var reactiveStoreMock: IReactiveStore<String, BtcPriceData>
    lateinit var remoteDataSourceMock: IRemoteDataSource
    lateinit var btcPriceSchemaMapperMock: IBtcSchemaMapper

    @Before
    fun setup() {
        reactiveStoreMock = mock()
        remoteDataSourceMock = mock()
        btcPriceSchemaMapperMock = mock()
        SUT = BtcPriceRepository(reactiveStoreMock, remoteDataSourceMock, btcPriceSchemaMapperMock)
    }


    //fetchLocal -> store is called
    @Test
    fun getBtcPrice_storeCalled() {
        SUT.getBtcPriceData(TIME_SPAN)
        verify(reactiveStoreMock).getSingular(any())
    }

    //fetchLocal -> network request is made
    @Test
    fun fetchBtcPrice_dataStored() {

        whenever(remoteDataSourceMock.getPriceFromNetwork(TIME_SPAN))
            .thenReturn(Single.just(createBtcPriceSchema()))

        whenever(btcPriceSchemaMapperMock.apply(any())).thenReturn(BtcPriceData())

        SUT.fetchBtcPrice(TIME_SPAN).subscribe()
        verify(reactiveStoreMock).storeSingular(any(), any())

    }

    private fun createBtcPriceSchema(): BtcPriceSchema {
        return BtcPriceSchema()
    }
}