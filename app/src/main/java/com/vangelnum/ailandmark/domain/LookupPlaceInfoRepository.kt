package com.vangelnum.ailandmark.domain

import com.vangelnum.ailandmark.data.feature_lookup.LookupPlaceInfo
import com.vangelnum.ailandmark.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface LookupPlaceInfoRepository {
    fun lookupPlaceInfo(id: String): Flow<Resource<List<LookupPlaceInfo>>>
}