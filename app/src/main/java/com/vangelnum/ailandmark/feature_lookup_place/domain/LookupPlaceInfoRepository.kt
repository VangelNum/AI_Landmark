package com.vangelnum.ailandmark.feature_lookup_place.domain

import com.vangelnum.ailandmark.feature_core.helpers.Resource
import com.vangelnum.ailandmark.feature_lookup_place.data.LookupPlaceInfo
import kotlinx.coroutines.flow.Flow

interface LookupPlaceInfoRepository {
    fun lookupPlaceInfo(id: String): Flow<Resource<List<LookupPlaceInfo>>>
}