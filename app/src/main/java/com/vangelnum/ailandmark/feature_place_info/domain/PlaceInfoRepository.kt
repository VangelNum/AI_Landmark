package com.vangelnum.ailandmark.feature_place_info.domain

import com.vangelnum.ailandmark.feature_core.helpers.Resource
import com.vangelnum.ailandmark.feature_place_info.data.PlaceResponse
import kotlinx.coroutines.flow.Flow

interface PlaceInfoRepository {
    fun getPlaceInfo(placeName: String): Flow<Resource<List<PlaceResponse>>>
}