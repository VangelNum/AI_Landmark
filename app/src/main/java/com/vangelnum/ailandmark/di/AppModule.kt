package com.vangelnum.ailandmark.di

import com.vangelnum.ailandmark.data.PlaceInfoApi
import com.vangelnum.ailandmark.data.PlaceInfoRepositoryImpl
import com.vangelnum.ailandmark.domain.PlaceInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofitPlaceInfo(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providePlaceInfoApi(retrofit: Retrofit): PlaceInfoApi {
        return retrofit.create(PlaceInfoApi::class.java)
    }

    @Singleton
    @Provides
    fun providePlaceRepository(api: PlaceInfoApi): PlaceInfoRepository {
        return PlaceInfoRepositoryImpl(api)
    }
}