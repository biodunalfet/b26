package com.hf.btcpricetracker.data

import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.theories.DataPoint
import org.junit.rules.ExpectedException
import java.lang.IllegalStateException

class BtcSchemaMapperTest {

    lateinit var SUT: IBtcSchemaMapper

    @Rule
    @JvmField
    var thrown = ExpectedException.none()

    @Before
    fun setUp() {
        SUT = BtcSchemaMapper()
    }

    @Test
    fun mapCorrectly() {

        val testData = createBtcPriceSchema()
        val result = SUT.apply(testData)

        result?.let {
            assertThat(it.unit, `is`(testData.unit))
            assertThat(it.period, `is`(testData.period))
            assertThat(it.values, `is`(testData.values))
        } ?: run {
            throw IllegalStateException("Invalid data returned")
        }

    }

    private fun createBtcPriceSchema(): BtcPriceSchema {

        return BtcPriceSchema(
            "USD", "day", "Average USD market price across major bitcoin exchanges",
            listOf(
                BtcPriceDataPoint("1558310400", "7926.704999999999"),
                BtcPriceDataPoint("1558313200", "7926.532524"),
                BtcPriceDataPoint("1538310400", "7926.704952299"),
                BtcPriceDataPoint("1958310400", "7926.7909999999"),
                BtcPriceDataPoint("1528310400", "7926.7049999199")
            )
        )

    }
}