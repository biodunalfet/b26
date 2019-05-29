package com.hf.btcpricetracker.data

import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocalStoreTest {

    lateinit var SUT: ILocalStore
    val EXISTING_TIMESPAN = "existing time span"
    val NON_EXISTENT_TIMESPAN = "non existent time span"

    @Before
    fun setUp() {
        SUT = LocalStore()
    }

    @Test
    fun existingKeyPassed_returnsData() {
        //arrange
        addData()
        //act
        val result = SUT.getLocalBtcPriceForTimeSpan(EXISTING_TIMESPAN)
        //asset
        assertThat(result, `is`(instanceOf(BtcPriceSchema::class.java)))
    }

    @Test
    fun nonExistentKeyPassed_returnsNUll() {
        //arrange
        deleteData()
        //act
        val result = SUT.getLocalBtcPriceForTimeSpan(NON_EXISTENT_TIMESPAN)
        //asset
        assertThat(result, `is`(nullValue()))
    }

    private fun deleteData() {
        SUT.deletePriceData(NON_EXISTENT_TIMESPAN)
    }

    private fun addData() {
        SUT.putBtcPriceData(EXISTING_TIMESPAN, BtcPriceSchema("id"))
    }

}