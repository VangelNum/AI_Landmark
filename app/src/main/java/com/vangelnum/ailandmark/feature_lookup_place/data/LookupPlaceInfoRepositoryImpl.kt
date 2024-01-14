package com.vangelnum.ailandmark.feature_lookup_place.data

import com.vangelnum.ailandmark.feature_core.helpers.Resource
import com.vangelnum.ailandmark.feature_lookup_place.domain.LookupPlaceInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LookupPlaceInfoRepositoryImpl @Inject constructor(
    private val api: LookupPlaceInfoApi
): LookupPlaceInfoRepository {
    override fun lookupPlaceInfo(id: String): Flow<Resource<List<LookupPlaceInfo>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getLookupPlaceInfo(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }
}