package com.hf.btcpricetracker

import android.app.Application
import com.hf.btcpricetracker.common.di.application.ApplicationComponent
import com.hf.btcpricetracker.common.di.application.ApplicationModule
import com.hf.btcpricetracker.common.di.application.DaggerApplicationComponent

class BtcApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }

}