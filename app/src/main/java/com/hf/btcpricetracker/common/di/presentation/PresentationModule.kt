package com.hf.btcpricetracker.common.di.presentation

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(val activity: Activity) {

    @Provides
    fun context(activity: Activity): Context {
        return activity
    }
}