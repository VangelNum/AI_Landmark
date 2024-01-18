package com.vangelnum.ailandmark.feature_history_search.di

import android.content.Context
import androidx.room.Room
import com.vangelnum.ailandmark.feature_history_search.data.SearchHistoryDao
import com.vangelnum.ailandmark.feature_history_search.data.SearchHistoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchHistoryModule {
    @Singleton
    @Provides
    fun provideSearchHistoryDatabase(@ApplicationContext context: Context): SearchHistoryDatabase {
        return Room.databaseBuilder(
            context,
            SearchHistoryDatabase::class.java,
            "search_history_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideSearchHistoryDao(database: SearchHistoryDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
}