package com.hf.btcpricetracker.presentation.priceTracker

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TimestampConverterTest {

    lateinit var SUT: TimestampConverter

    @Before
    fun setUp() {
        SUT = TimestampConverter()
    }

    @Test
    fun validDatesPassed_returnsCorrectFormattedDate() {

        val testData = createTestData()

        for (i in testData) {
            val result = SUT.convertToFormattedDate(i.first)
            assertThat(result, `is`(i.second))
        }
    }

    @Test
    fun invalidDatePassed_returnsNA() {

        val testData = "n26"
        val result = SUT.convertToFormattedDate(testData)
        assertThat(result, `is`("N/A"))
    }

    private fun createTestData(): List<Pair<String, String>> {

        return listOf(
            "1558310400" to "20/05/19",
            "1316217600" to "17/09/11",
            "1289260800" to "09/11/10",
            "1486512000" to "08/02/17",
            "1514764800" to "01/01/18"
        )
    }
}