package com.hf.btcpricetracker.common.di.application

import com.hf.btcpricetracker.common.di.presentation.PresentationComponent
import com.hf.btcpricetracker.common.di.presentation.PresentationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {
    abstract fun newPresentationComponent(presentationModule: PresentationModule): PresentationComponent
}