package com.vangelnum.ailandmark.feature_history_search.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history_table ORDER BY id DESC")
    fun getAllSearchHistory(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToSearchHistory(place: SearchHistoryEntity)

    @Delete
    suspend fun deleteFromSearchHistory(place: SearchHistoryEntity)

    @Query("DELETE FROM search_history_table")
    suspend fun deleteAllHistory()
}