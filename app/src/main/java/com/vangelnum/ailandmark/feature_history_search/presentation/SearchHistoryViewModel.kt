package com.vangelnum.ailandmark.feature_history_search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vangelnum.ailandmark.feature_core.helpers.Resource
import com.vangelnum.ailandmark.feature_history_search.data.SearchHistoryEntity
import com.vangelnum.ailandmark.feature_history_search.domain.SearchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchHistoryViewModel @Inject constructor(
    private val repository: SearchHistoryRepository
): ViewModel() {
    private val _searchHistoryState = MutableStateFlow<Resource<List<SearchHistoryEntity>>>(Resource.Loading)
    val searchHistoryState = _searchHistoryState.asStateFlow()

    init {
        getAllSearchHistory()
    }

    private fun getAllSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSearchHistory().collect {
                _searchHistoryState.value = it
            }
        }
    }

    fun insertToSearchHistory(placeInfo: SearchHistoryEntity) {
        viewModelScope.launch {
            repository.insertToSearchHistory(placeInfo)
        }
    }

    fun deleteFromSearchHistory(placeInfo: SearchHistoryEntity) {
        viewModelScope.launch {
            repository.deleteFromSearchHistory(placeInfo)
        }
    }

    fun deleteAllSearchHistory() {
        viewModelScope.launch {
            repository.deleteAllHistory()
        }
    }
}