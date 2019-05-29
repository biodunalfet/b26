package com.hf.btcpricetracker.common.di.application

import android.app.Application
import com.hf.btcpricetracker.common.networking.BtcService
import com.hf.btcpricetracker.data.*
import com.hf.btcpricetracker.domain.RetrievePriceInteractor
import com.hf.btcpricetracker.presentation.priceTracker.MoneyFormatter
import com.hf.btcpricetracker.presentation.priceTracker.TimestampConverter
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Module
class ApplicationModule(val application: Application) {

    @Singleton
    @Provides
    fun providesInteractor(repository: BtcPriceRepository): RetrievePriceInteractor {
        return RetrievePriceInteractor(repository)
    }

    @Singleton
    @Provides
    fun providesRemoteDataSource(service: BtcService): RemoteDataSource {
        return RemoteDataSource(service)
    }

    @Singleton
    @Provides
    fun providesTimestampProvider(): TimeStampProvider {
        return TimeStampProvider()
    }

    @Singleton
    @Provides
    fun providesTimestampConverter(): TimestampConverter {
        return TimestampConverter()
    }

    @Singleton
    @Provides
    fun providesMoneyFormatter(): MoneyFormatter {
        return MoneyFormatter()
    }

    @Singleton
    @Provides
    fun providesCache(timeStampProvider: TimeStampProvider): Cache<String, BtcPriceData> {
        return Cache(timeStampProvider, 1000)
    }

    @Singleton
    @Provides
    fun providesReactiveStore(cache: Cache<String, BtcPriceData>): ReactiveStore<String, BtcPriceData> {
        return ReactiveStore(cache)
    }

    @Singleton
    @Provides
    fun providesBtcSchemaMapper(): BtcSchemaMapper {
        return BtcSchemaMapper()
    }

    @Singleton
    @Provides
    fun providesRepository(
        reactiveStore: ReactiveStore<String,
                BtcPriceData>, dataSource: RemoteDataSource,
        btcSchemaMapper: BtcSchemaMapper
    ): BtcPriceRepository {
        return BtcPriceRepository(reactiveStore, dataSource, btcSchemaMapper)
    }
}