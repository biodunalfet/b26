package com.hf.btcpricetracker.common.di.presentation

import com.hf.btcpricetracker.presentation.priceTracker.PriceTrackerActivity
import dagger.Subcomponent

@Subcomponent(modules = [PresentationModule::class, ViewModelModule::class])
interface PresentationComponent {
    fun inject(activity: PriceTrackerActivity)
}