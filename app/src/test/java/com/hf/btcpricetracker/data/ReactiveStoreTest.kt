package com.hf.btcpricetracker.data

import com.hf.btcpricetracker.common.RxSchedulerOverrideRule
import com.nhaarman.mockitokotlin2.*
import com.petertackage.kotlinoptions.optionOf
import io.reactivex.Maybe
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertThat

class ReactiveStoreTest {

    @Rule
    @JvmField
    val overrideSchedulersRule = RxSchedulerOverrideRule()

    lateinit var SUT: ReactiveStore<String, TestObject>
    lateinit var cacheMock: ICache<String, TestObject>

    @Before
    fun setUp() {
        cacheMock = mock()
        SUT = ReactiveStore(cacheMock)
    }

    //none is emitted when cache is empty
    @Test
    fun cacheIsEmpty_noneEmitted() {
        //arrange
        whenever(cacheMock.getAll()).thenReturn(Maybe.empty())
        whenever(cacheMock.getSingular(any())).thenReturn(Maybe.empty())
        //act
        val result2 = SUT.getSingular("key")
        result2.test().assertNoValues()

        val result1 = SUT.getAll()

        //assert
        val testObserver = result1.test()
        testObserver.assertNoValues()
    }

    //store all -> data emitted once to subscriber
    @Test
    fun storeSingular_lastDataEmitted() {
        //arrange
        val data1 = TestObject(1)
        val data2 = TestObject(2)
        whenever(cacheMock.getAll()).thenReturn(Maybe.just(listOf("id1" to data1)))
        whenever(cacheMock.getSingular("id1")).thenReturn(Maybe.just("id1" to data1))
        //act
        SUT.storeSingular("id1", data1)

        whenever(cacheMock.getAll()).thenReturn(Maybe.just(createTestList()))
        whenever(cacheMock.getSingular("id1")).thenReturn(Maybe.just("id1" to data2))
        SUT.storeSingular("id1", data2)

        //assert
        SUT.getSingular("id1").test().assertValue(optionOf("id1" to data2))
    }


    //when replace called observer for getSingular (id) should be called because the id value has changed
    @Test
    fun replaceAll_getAllCallStreamEmittedAfterReplaced() {
        //arrange
        val testObjs1 = listOf(
            "id1" to TestObject(1),
            "id2" to TestObject(2),
            "id3" to TestObject(3)
        )

        val testObjs2 = listOf(
            "id1" to TestObject(4),
            "id2" to TestObject(5234),
            "id3" to TestObject(224),
            "id4" to TestObject(334)
        )

        whenever(cacheMock.getAll()).thenReturn(Maybe.just(testObjs1))
        val testObserver = SUT.getAll().test()

        whenever(cacheMock.getAll()).thenReturn(Maybe.just(testObjs2))
        //act
        SUT.replaceAll(testObjs2)

        //assert
        testObserver.assertValueAt(1) { it ->
            it.equals(optionOf(testObjs2.map { it.second }))
        }

    }

    //store all is called, observer of old data that has been overridden should be called
    @Test
    fun storeAll_singularStreamEmittedAfterStored() {
        val testObject = TestObject(4)

        whenever(cacheMock.getSingular("id4")).thenReturn(Maybe.just("id4" to testObject))

        val testObserver = SUT.getSingular("id4").test()

        val testObjs2 = listOf(
            "id1" to TestObject(54),
            "id2" to TestObject(5234),
            "id3" to TestObject(224),
            "id4" to testObject
        )

        whenever(cacheMock.getAll()).thenReturn(Maybe.just(testObjs2))

        SUT.storeAll(testObjs2)

        testObserver.assertValueAt(1) {
            it.equals(optionOf("id4" to testObject))
        }

    }

    @Test
    fun storeSingular_correctParametersPassedToCache() {

        val key = "id5"
        val value = TestObject(5)

        whenever(cacheMock.getAll()).thenReturn(Maybe.just(listOf("id1" to TestObject(4))))

        whenever(cacheMock.getSingular(key)).thenReturn(Maybe.just("id1" to value))
        SUT.storeSingular(key, value)

        argumentCaptor<String>().apply {
            verify(cacheMock).putSingular(capture(), eq(value))
            assertThat(key, `is`(allValues[0]))
        }

        argumentCaptor<TestObject>().apply {
            verify(cacheMock).putSingular(eq(key), capture())
            assertThat(value, `is`(allValues[0]))
        }

    }

    @Test
    fun storeAll_correctParametersPassedToCache() {

        val data = createTestList()

        whenever(cacheMock.getAll()).thenReturn(Maybe.just(listOf("id1" to TestObject(4))))

        SUT.storeAll(data)

        argumentCaptor<List<Pair<String, TestObject>>>().apply {
            verify(cacheMock).putAll(capture())
            assertThat(data, `is`(allValues[0]))
        }

    }

    @Test
    fun replaceAll_correctParametersPassedToCache() {

        val data = createTestList()

        whenever(cacheMock.getAll()).thenReturn(Maybe.just(listOf("id1" to TestObject(4))))

        SUT.replaceAll(data)

        argumentCaptor<List<Pair<String, TestObject>>>().apply {
            verify(cacheMock).putAll(capture())
            assertThat(data, `is`(allValues[0]))
        }
    }

    private fun createTestList(): List<Pair<String, TestObject>> {
        return listOf(
            "id1" to TestObject(54),
            "id2" to TestObject(5234),
            "id3" to TestObject(224)
        )
    }

}

data class TestObject(val value: Int)