package com.vangelnum.ailandmark.feature_lookup_place.di

import com.vangelnum.ailandmark.feature_lookup_place.data.LookupPlaceInfoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LookupPlaceModule {
    @Singleton
    @Provides
    fun provideLookUpApi(retrofit: Retrofit): LookupPlaceInfoApi {
        return retrofit.create(LookupPlaceInfoApi::class.java)
    }
}