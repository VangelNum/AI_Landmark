package com.vangelnum.ailandmark.feature_place_info.di

import com.vangelnum.ailandmark.feature_place_info.data.PlaceInfoRepositoryImpl
import com.vangelnum.ailandmark.feature_place_info.domain.PlaceInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PlaceInfoRepositoryModule {
    @Binds
    @Singleton
    fun bindLookupRepository(placeInfoRepositoryImpl: PlaceInfoRepositoryImpl): PlaceInfoRepository
}