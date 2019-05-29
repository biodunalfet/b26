package com.hf.btcpricetracker.presentation.priceTracker

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.any
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.Rule
import java.lang.IllegalStateException


class ChartTimeSpanMapperTest {

    lateinit var SUT: ChartTimeSpanMapper

    @Rule
    @JvmField
    val exception = ExpectedException.none()

    @Before
    fun setUp() {
        SUT = ChartTimeSpanMapper()
    }

    @Test
    fun weekTimeSpanPassed_returnCorrectlyMappedData() {

        val testData = TIMESPAN.WEEK

        val result = SUT.mapTimeSpanToParam(testData)
        assertThat("1week", `is`(result))

    }

    @Test
    fun monthTimeSpanPassed_returnCorrectlyMappedData() {

        val testData = TIMESPAN.MONTH

        val result = SUT.mapTimeSpanToParam(testData)
        assertThat("1months", `is`(result))

    }

    @Test
    fun yearTimeSpanPassed_returnCorrectlyMappedData() {

        val testData = TIMESPAN.YEAR

        val result = SUT.mapTimeSpanToParam(testData)
        assertThat("1year", `is`(result))

    }

    @Test
    fun allTimeSpanPassed_returnCorrectlyMappedData() {

        val testData = TIMESPAN.ALL

        val result = SUT.mapTimeSpanToParam(testData)
        assertThat("all", `is`(result))

    }
}