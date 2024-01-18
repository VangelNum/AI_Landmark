package com.vangelnum.ailandmark.feature_history_search.di

import com.vangelnum.ailandmark.feature_history_search.data.SearchHistoryRepositoryImpl
import com.vangelnum.ailandmark.feature_history_search.domain.SearchHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SearchHistoryRepositoryModule {
    @Binds
    @Singleton
    fun bindSearchHistoryRepository(
        searchHistoryRepositoryImpl: SearchHistoryRepositoryImpl
    ): SearchHistoryRepository
}