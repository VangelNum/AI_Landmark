package com.vangelnum.ailandmark.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vangelnum.ailandmark.data.feature_lookup.LookupPlaceInfoApi
import com.vangelnum.ailandmark.data.feature_lookup.LookupPlaceInfoRepositoryImpl
import com.vangelnum.ailandmark.data.feature_place_info.PlaceInfoApi
import com.vangelnum.ailandmark.data.feature_place_info.PlaceInfoRepositoryImpl
import com.vangelnum.ailandmark.domain.LookupPlaceInfoRepository
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

    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    @Singleton
    @Provides
    fun provideRetrofitPlaceInfo(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(GsonConverterFactory.create(gson))
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


    @Singleton
    @Provides
    fun provideLookUpApi(retrofit: Retrofit): LookupPlaceInfoApi {
        return retrofit.create(LookupPlaceInfoApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLookupPlaceRepository(api: LookupPlaceInfoApi): LookupPlaceInfoRepository {
        return LookupPlaceInfoRepositoryImpl(api)
    }
}