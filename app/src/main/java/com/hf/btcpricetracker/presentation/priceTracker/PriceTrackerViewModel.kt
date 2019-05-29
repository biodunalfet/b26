package com.hf.btcpricetracker.presentation.priceTracker

import android.graphics.Color
import androidx.lifecycle.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.hf.btcpricetracker.data.BtcPriceData
import com.hf.btcpricetracker.data.BtcPriceDataPoint
import com.hf.btcpricetracker.domain.RetrievePriceInteractor
import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


enum class TIMESPAN {
    WEEK, MONTH, YEAR, ALL
}

class PriceTrackerViewModel @Inject constructor(
    private val interactor: RetrievePriceInteractor,
    private val timeSpanMapper: ChartTimeSpanMapper,
    private val timestampConverter: TimestampConverter,
    private val moneyFormatter: MoneyFormatter
) : ViewModel() {

    val priceTimePair: MutableLiveData<Pair<String, String>> = MutableLiveData()
    private val currentBtcPriceData: MutableLiveData<Pair<String, BtcPriceData>> = MutableLiveData()
    val graphData: MediatorLiveData<LineData> = MediatorLiveData()
    var currentSpan: String? = null

    fun getPrices(timeSpan: TIMESPAN) {

        graphData.value = null
        val timespanParam = timeSpanMapper.mapTimeSpanToParam(timeSpan)
        currentSpan = timespanParam

        val btcPriceTimeData = LiveDataReactiveStreams.fromPublisher(
            interactor.getPriceStream(timespanParam)
                .observeOn(Schedulers.computation()).toFlowable(BackpressureStrategy.LATEST)
        )

        graphData.removeSource(btcPriceTimeData)

        graphData.addSource(btcPriceTimeData) {

            if (it.first == currentSpan) {

                currentBtcPriceData.value = it

                val entries = it.second.values?.mapIndexed { index, btcPriceDataPoint ->
                    Entry(
                        index.toFloat(),
                        btcPriceDataPoint.y.toFloat()
                    )
                }
                val set = addEntriesToSet(entries)
                val data = LineData(set).apply {
                    setValueTextSize(9f)
                    setDrawValues(false)
                }

                graphData.postValue(data)
                graphData.removeSource(btcPriceTimeData)

                it.second.values?.let {
                    if (it.isNotEmpty()) {
                        val latestPriceDataPoint = it.last()
                        showTimePriceText(latestPriceDataPoint)
                    }
                }
            }
        }
    }

    fun onNoDataPointSelected() {

        currentBtcPriceData.value?.let {

            if (it.first == currentSpan) {
                it.second.values?.let {
                    if (it.isNotEmpty()) {
                        val latestPriceDataPoint = it.last()
                        showTimePriceText(latestPriceDataPoint)
                    }
                }
            }
        }
    }

    fun onDataPointSelected(entry: Entry) {
        currentBtcPriceData.value?.let {
            val index = entry.x.toInt()
            if (it.first == currentSpan && !listOf(-1, null).contains(index)) {

                it.second.values?.let { dataPoints ->
                    val point = dataPoints[index]
                    showTimePriceText(point)
                }
            }
        }
    }

    private fun addEntriesToSet(entries: List<Entry>?): LineDataSet {

        val set = LineDataSet(entries, null)

        set.apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.2f
            setDrawFilled(true)
            setDrawCircles(true)
            lineWidth = 1.8f
            circleRadius = 4f
            setCircleColor(Color.BLACK)
            highLightColor = Color.parseColor("#455a64")
            color = Color.parseColor("#455a64")
            fillColor = Color.parseColor("#718792")
        }

        return set

    }

    private fun showTimePriceText(priceDataPoint: BtcPriceDataPoint) {
        val dateForPrice = timestampConverter.convertToFormattedDate(priceDataPoint.x)
        val priceInDollars = moneyFormatter.formatMoney(priceDataPoint.y)
        priceTimePair.value = priceInDollars to dateForPrice
    }


}