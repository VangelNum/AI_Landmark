package com.vangelnum.ailandmark.domain

import com.vangelnum.ailandmark.data.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceInfoApi {
    @GET("search")
    suspend fun getPlaceInfo(
        @Query("q") query: String,
        @Query("format") format: String = "json"
    ): List<PlaceResponse>
}
