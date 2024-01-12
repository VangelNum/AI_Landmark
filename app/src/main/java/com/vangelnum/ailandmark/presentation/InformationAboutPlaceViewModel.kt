package com.vangelnum.ailandmark.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vangelnum.ailandmark.data.PlaceResponse
import com.vangelnum.ailandmark.domain.PlaceInfoRepository
import com.vangelnum.ailandmark.helpers.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InformationAboutPlaceViewModel @Inject constructor(
    private val repository: PlaceInfoRepository
) : ViewModel() {

    private val _placeResponse = MutableStateFlow<Resource<List<PlaceResponse>>>(Resource.Empty)
    val placeInfo = _placeResponse.asStateFlow()

    fun getPlaceInfo(placeName: String) {
        viewModelScope.launch {
            repository.getPlaceInfo(placeName).collect {
                _placeResponse.value = it
            }
        }
    }
}