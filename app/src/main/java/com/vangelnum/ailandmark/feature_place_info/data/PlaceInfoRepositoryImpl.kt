package com.vangelnum.ailandmark.feature_place_info.data

import com.vangelnum.ailandmark.feature_core.helpers.Resource
import com.vangelnum.ailandmark.feature_place_info.domain.PlaceInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaceInfoRepositoryImpl @Inject constructor(
    private val api: PlaceInfoApi
) : PlaceInfoRepository {
    override fun getPlaceInfo(placeName: String): Flow<Resource<List<PlaceResponse>>> = flow {
        emit(Resource.Loading)
        try {
            val result = api.getPlaceInfo(placeName)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error("An error occurred: ${e.message}"))
        }
    }
}
