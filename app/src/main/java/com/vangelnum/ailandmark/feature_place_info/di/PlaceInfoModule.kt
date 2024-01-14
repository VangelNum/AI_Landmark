package com.vangelnum.ailandmark.feature_place_info.di

import com.vangelnum.ailandmark.feature_place_info.data.PlaceInfoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaceInfoModule {
    @Singleton
    @Provides
    fun providePlaceInfoApi(retrofit: Retrofit): PlaceInfoApi {
        return retrofit.create(PlaceInfoApi::class.java)
    }
}