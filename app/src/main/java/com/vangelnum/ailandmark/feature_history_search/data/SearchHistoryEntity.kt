package com.vangelnum.ailandmark.feature_history_search.data

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(tableName = "search_history_table")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val score: Float,
)