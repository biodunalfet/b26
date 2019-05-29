package com.hf.btcpricetracker.presentation.priceTracker

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MoneyFormatterTest {

    lateinit var SUT: MoneyFormatter

    @Before
    fun setUp() {
        SUT = MoneyFormatter()
    }

    @Test
    fun validAmounts_FormattedStringReturned() {

        val testData = createTestData()

        for (i in testData) {

            val result = SUT.formatMoney(i.first)
            assertThat(i.second, `is`(result))
        }
    }

    @Test
    fun invalidAmountPassed_NAReturned() {

        val testData = createInvalidTestData()

        for (i in testData) {

            val result = SUT.formatMoney(i.first)
            assertThat(i.second, `is`(result))
        }

    }

    fun createInvalidTestData(): List<Pair<String, String>> {
        return listOf(
            "" to "N/A",
            "Bca" to "N/A",
            "." to "N/A"
        )
    }

    fun createTestData(): List<Pair<String, String>> {
        return listOf(
            "13424.000" to "$13,424.00",
            "6316.881666666667" to "$6,316.88",
            "8223.288333333334" to "$8,223.29",
            "10532.791666666666" to "$10,532.79",
            "7130.541666666667" to "$7,130.54"
        )
    }
}