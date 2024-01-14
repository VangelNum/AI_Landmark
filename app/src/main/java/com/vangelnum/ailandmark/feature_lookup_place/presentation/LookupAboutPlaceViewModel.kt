package com.vangelnum.ailandmark.feature_lookup_place.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vangelnum.ailandmark.feature_core.helpers.Resource
import com.vangelnum.ailandmark.feature_lookup_place.data.LookupPlaceInfo
import com.vangelnum.ailandmark.feature_lookup_place.domain.LookupPlaceInfoRepository
import com.vangelnum.ailandmark.feature_place_info.domain.PlaceInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LookupAboutPlaceViewModel @Inject constructor(
    private val repository: PlaceInfoRepository,
    private val lookupRepository: LookupPlaceInfoRepository
) : ViewModel() {

    private val _allPlaceInformation = MutableStateFlow<Resource<List<LookupPlaceInfo>>>(Resource.Loading)
    val allPlaceInformation = _allPlaceInformation.asStateFlow()

    fun getListOfPlacesWithName(placeName: String) {
        viewModelScope.launch {
            _allPlaceInformation.value = Resource.Loading
            val lookupList = mutableListOf<LookupPlaceInfo>()
            repository.getPlaceInfo(placeName).collect { resourceResponse->
                when (resourceResponse) {
                    is Resource.Error -> {
                        _allPlaceInformation.value = resourceResponse
                    }

                    is Resource.Loading -> {
                        _allPlaceInformation.value = resourceResponse
                    }

                    is Resource.Empty -> {
                        _allPlaceInformation.value = resourceResponse
                    }

                    is Resource.Success -> {
                        resourceResponse.data.forEach { placeResponse ->
                            val type = placeResponse.osmType.first().uppercase()
                            val id = placeResponse.osmId.toString()
                            val collectedString = type + id
                            lookupPlacesInfo(collectedString).collect { lookupResponse ->
                                if (lookupResponse is Resource.Success) {
                                    lookupList.addAll(lookupResponse.data)
                                }
                            }
                        }
                        _allPlaceInformation.value = Resource.Success(lookupList.toList())
                    }
                }
            }
        }
    }

    private fun lookupPlacesInfo(id: String): Flow<Resource<List<LookupPlaceInfo>>> {
        return lookupRepository.lookupPlaceInfo(id)
    }
}
