package com.vangelnum.ailandmark.data.feature_lookup

import com.vangelnum.ailandmark.domain.LookupPlaceInfoRepository
import com.vangelnum.ailandmark.helpers.Resource
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