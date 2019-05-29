package com.hf.btcpricetracker.domain

import com.hf.btcpricetracker.data.BtcPriceData
import com.hf.btcpricetracker.data.BtcPriceRepository
import com.hf.btcpricetracker.data.IBtcPriceRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.petertackage.kotlinoptions.Option
import com.petertackage.kotlinoptions.optionOf
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.BehaviorSubject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class RetrievePriceInteractorTest {

    lateinit var SUT: RetrievePriceInteractor
    lateinit var repositoryMock: IBtcPriceRepository
    lateinit var ts: TestObserver<Pair<String, BtcPriceData>>

    private val repoBtcPriceStream = BehaviorSubject.create<Option<Pair<String, BtcPriceData>>>()


    @Before
    fun setUp() {
        ts = TestObserver()
        repositoryMock = mock()
        SUT = RetrievePriceInteractor(repositoryMock)
    }

    @Test
    fun priceDataFromRepoIsUnwrappedAndPassedOn() {

        //Arrange
        val testData = createPriceData()
        whenever(repositoryMock.getBtcPriceData(any())).thenReturn(repoBtcPriceStream)
        repoBtcPriceStream.onNext(optionOf(Mockito.any(String::class.java) to testData))

        //Act
        SUT.getPriceStream("week").subscribe(ts)

        //Assert
        verify(repositoryMock).getBtcPriceData(any())
        ts.assertNotTerminated()
    }

    @Test
    fun priceDataFromRepo_fetchBtcPriceDataCalled() {

        //Arrange
        val testData = createPriceData()
        whenever(repositoryMock.getBtcPriceData(any())).thenReturn(repoBtcPriceStream)
        repoBtcPriceStream.onNext(optionOf(Mockito.any(String::class.java) to testData))

        //Act
        SUT.getPriceStream("week").subscribe(ts)

        //Assert
        verify(repositoryMock).fetchBtcPrice(any())
        ts.assertNotTerminated()
    }

    private fun createPriceData(): BtcPriceData {
        return BtcPriceData()
    }
}