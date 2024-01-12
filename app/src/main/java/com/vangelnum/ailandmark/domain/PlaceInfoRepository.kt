package com.vangelnum.ailandmark.domain

import com.vangelnum.ailandmark.data.PlaceResponse
import com.vangelnum.ailandmark.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface PlaceInfoRepository {
    fun getPlaceInfo(placeName: String): Flow<Resource<List<PlaceResponse>>>
}