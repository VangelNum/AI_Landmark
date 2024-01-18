package com.vangelnum.ailandmark.feature_history_search.data

import com.vangelnum.ailandmark.feature_core.helpers.Resource
import com.vangelnum.ailandmark.feature_history_search.domain.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val dao: SearchHistoryDao
) : SearchHistoryRepository {
    override fun getSearchHistory(): Flow<Resource<List<SearchHistoryEntity>>> = flow {
        try {
            dao.getAllSearchHistory().collect {
                emit(Resource.Success(it))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun insertToSearchHistory(place: SearchHistoryEntity) {
        dao.insertToSearchHistory(place)
    }

    override suspend fun deleteFromSearchHistory(place: SearchHistoryEntity) {
        dao.deleteFromSearchHistory(place)
    }

    override suspend fun deleteAllHistory() {
        dao.deleteAllHistory()
    }
}