package com.vangelnum.ailandmark.feature_lookup_place.di

import com.vangelnum.ailandmark.feature_lookup_place.data.LookupPlaceInfoRepositoryImpl
import com.vangelnum.ailandmark.feature_lookup_place.domain.LookupPlaceInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LookupRepositoryBind {
    @Binds
    @Singleton
    fun bindLookupRepository(lookupPlaceRepositoryImpl: LookupPlaceInfoRepositoryImpl): LookupPlaceInfoRepository
}