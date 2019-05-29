package com.hf.btcpricetracker.common.di.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hf.btcpricetracker.domain.RetrievePriceInteractor
import com.hf.btcpricetracker.presentation.common.viewmodel.ViewModelFactory
import com.hf.btcpricetracker.presentation.priceTracker.ChartTimeSpanMapper
import com.hf.btcpricetracker.presentation.priceTracker.MoneyFormatter
import com.hf.btcpricetracker.presentation.priceTracker.PriceTrackerViewModel
import com.hf.btcpricetracker.presentation.priceTracker.TimestampConverter
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import kotlin.reflect.KClass

@Module
class ViewModelModule {

    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    @MapKey
    annotation class ViewModelKey(val value: KClass<out ViewModel>)


    @Provides
    @IntoMap
    @ViewModelKey(PriceTrackerViewModel::class)
    fun providePriceTrackerViewModel(
        interactor: RetrievePriceInteractor,
        timeSpanMapper: ChartTimeSpanMapper,
        timestampConverter: TimestampConverter,
        moneyFormatter: MoneyFormatter
    ): ViewModel {
        return PriceTrackerViewModel(interactor, timeSpanMapper, timestampConverter, moneyFormatter)
    }

    @Provides
    fun provideViewModelFactory(providerMap: MutableMap<Class<out ViewModel>, Provider<ViewModel>>): ViewModelFactory {
        return ViewModelFactory(providerMap)
    }
}