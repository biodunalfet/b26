package com.hf.btcpricetracker.presentation.priceTracker

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.hf.btcpricetracker.R
import com.hf.btcpricetracker.presentation.common.activities.BaseActivity
import com.hf.btcpricetracker.presentation.common.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject


class PriceTrackerActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: PriceTrackerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPresentationComponent().inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[PriceTrackerViewModel::class.java]

        setUpChart()
        setUpObservers()
        viewModel.getPrices(TIMESPAN.WEEK)
        setUpToggleBtn()

    }

    private fun setUpToggleBtn() {

        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    week1Btn.id -> viewModel.getPrices(TIMESPAN.WEEK)
                    month1Btn.id -> viewModel.getPrices(TIMESPAN.MONTH)
                    year1Btn.id -> viewModel.getPrices(TIMESPAN.YEAR)
                    allTimeBtn.id -> viewModel.getPrices(TIMESPAN.ALL)
                }
            }
        }

        toggleGroup.check(week1Btn.id)

        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                viewModel.onNoDataPointSelected()
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e?.let {
                    viewModel.onDataPointSelected(it)
                }
            }

        })
    }

    private fun setUpChart() {
        chart.apply {
            axisLeft.setDrawGridLines(false)
            axisRight.setDrawGridLines(false)
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            xAxis.setDrawGridLines(false)
            setDrawGridBackground(false)
            description = Description().apply { text = "" }
            legend.isEnabled = false
            xAxis.setDrawLabels(false)
            axisRight.setDrawLabels(false)
            axisLeft.setDrawLabels(false)
            isHighlightPerTapEnabled = true
        }
    }

    private fun setUpObservers() {

        viewModel.priceTimePair.observe(this, Observer {
            it?.let {
                priceTv.text = it.first
                timeTv.text = it.second
            }
        })

        viewModel.graphData.observe(this, Observer {
            it?.let {
                chart.data = it
                chart.data.notifyDataChanged()
                chart.notifyDataSetChanged()
                chart.invalidate()
            }
                ?: run {
                    chart.setNoDataText("")
                    chart.clear()
                    chart.notifyDataSetChanged()
                    chart.clear()
                    chart.invalidate()
                }

        })

    }

}
