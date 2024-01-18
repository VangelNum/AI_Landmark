package com.vangelnum.ailandmark.feature_history_search.domain

import com.vangelnum.ailandmark.feature_core.helpers.Resource
import com.vangelnum.ailandmark.feature_history_search.data.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow


interface SearchHistoryRepository {
    fun getSearchHistory(): Flow<Resource<List<SearchHistoryEntity>>>
    suspend fun insertToSearchHistory(place: SearchHistoryEntity)
    suspend fun deleteFromSearchHistory(place: SearchHistoryEntity)
    suspend fun deleteAllHistory()
}