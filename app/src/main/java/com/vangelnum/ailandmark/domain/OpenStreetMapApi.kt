package com.vangelnum.ailandmark.domain

import com.vangelnum.ailandmark.data.PlaceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenStreetMapService {
    @GET("search")
    suspend fun getPlaceInfo(
        @Query("q") query: String,
        @Query("format") format: String = "json"
    ): Response<List<PlaceResponse>>
}
