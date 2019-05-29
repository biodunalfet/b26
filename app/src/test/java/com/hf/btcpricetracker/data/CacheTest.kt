package com.hf.btcpricetracker.data

import com.hf.btcpricetracker.common.RxSchedulerOverrideRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit


class CacheTest {


    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulerOverrideRule()

    lateinit var SUT: Cache<String, Int>

    var timeStampProviderMock: ITimeStampProvider = mock()
    val timeOutMs: Long = 1000
    val EXIST_KEY = "exists"
    val NOT_EXIST_KEY = "exists"

    val random by lazy {
        Random()
    }

    @Before
    fun setUp() {
        SUT = Cache(timeStampProviderMock, timeOutMs)
    }

    // get data with key that exists -> emits value if not expired
    @Test
    fun getSingularDataWithKeyThatExists_EmitValueUnexpired() {
        //arrange
        val data = singularObjectWithTime(100)
        //act
        whenever(timeStampProviderMock.currentTimeInMs()).thenReturn(150)
        val result = SUT.getSingular(EXIST_KEY)
        //assert
        result.test().assertValue(EXIST_KEY to data)
    }

    // get data with key that does not exists -> emits nothing
    @Test
    fun getSingularDataWithKeyThatDoesNotExists_EmitNothing() {
        //act
        whenever(timeStampProviderMock.currentTimeInMs()).thenReturn(200)
        val result = SUT.getSingular(NOT_EXIST_KEY)
        //assert
        result.test().assertNoValues()
    }

    // get data with key that exists -> emits value if not expired
    @Test
    fun getSingularDataWithKeyThatExistsButExpiredValue_EmitNothing() {
        //arrange
        val data = singularObjectWithTime(100)
        //act
        whenever(timeStampProviderMock.currentTimeInMs()).thenReturn(200000)
        val result = SUT.getSingular(EXIST_KEY)
        //assert
        result.test().assertNoValues()
    }

    // get all data -> emits only values that have not expired
    @Test
    fun getAllData_EmitValueUnexpired() {
        //arrange
        val dataUnExpired1 = random.nextInt()
        val dataExpired1 = random.nextInt()
        val dataUnExpired2 = random.nextInt()

        val data = mutableListOf<Pair<String, Int>>()
        data.add("E1" to dataUnExpired1)
        data.add("E2" to dataUnExpired2)
        data.add("E3" to dataExpired1)

        val expiryTimes = listOf<Long>(110, 123, 1424324)

        data.forEachIndexed { index, pair ->
            whenever(timeStampProviderMock.currentTimeInMs()).thenReturn(expiryTimes[index])
            SUT.putSingular(pair.first, pair.second)
        }

        //act
        whenever(timeStampProviderMock.currentTimeInMs()).thenReturn(200)
        val result = SUT.getAll()
        //assert
        result.test().assertValues(data)
    }

    // get all data -> emits nothing because all data has expired
    @Test
    fun getAllDataFromCacheWithAllExpired_EmitNothing() {
        //arrange
        val dataExpired1 = random.nextInt()
        val dataExpired2 = random.nextInt()
        val dataExpired3 = random.nextInt()

        val data = mutableListOf<Pair<String, Int>>()
        data.add("E1" to dataExpired1)
        data.add("E2" to dataExpired2)
        data.add("E3" to dataExpired3)

        val expiryTimes = listOf<Long>(45334, 53325, 14324)

        data.forEachIndexed { index, pair ->
            whenever(timeStampProviderMock.currentTimeInMs()).thenReturn(expiryTimes[index])
            SUT.putSingular(pair.first, pair.second)
        }

        //act
        whenever(timeStampProviderMock.currentTimeInMs()).thenReturn(20453245320)
        val result = SUT.getAll()
        //assert
        result.test().assertNoValues()
    }

    // save data -> store contains it
    @Test
    fun storeData_StoreContainsData() {
        //act
        val key = "k1"
        val data = 678
        SUT.putSingular(key, data)
        //assert
        SUT.getSingular(key).test().assertValue(key to data)
    }

    // save collection -> store contains them all
    @Test
    fun storeCollection_ContainsAllData() {
        //act
        val data = mutableListOf<Pair<String, Int>>(
            "a1" to 34, "a2" to 243, "a3" to 419
        )
        SUT.putAll(data)
        //assert
        SUT.getAll().test().assertValue(data)
    }

    // delete data -> store does not contain
    @Test
    fun deleteSingular_ItemIsNotPresent() {
        //act
        singularObjectWithTime()
        SUT.deleteSingular(EXIST_KEY)
        //assert
        SUT.getSingular(EXIST_KEY).test().assertNoValues()
    }

    // delete all -> store is empty
    @Test
    fun deleteAll_ItemIsNotPresent() {
        //arrange
        val data = mutableListOf(
            "a1" to 34, "a2" to 243, "a3" to 419
        )
        SUT.putAll(data)
        //act
        SUT.deleteAll()
        //assert
        SUT.getAll().test().assertNoValues()
    }

    private fun singularObjectWithTime(time: Long = random.nextLong()): Int {
        whenever(timeStampProviderMock.currentTimeInMs()).thenReturn(time)
        val data = random.nextInt()
        SUT.putSingular(EXIST_KEY, data)
        return data
    }

}