package com.vangelnum.ailandmark.feature_place_info.data

import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceInfoApi {
    @GET("search")
    suspend fun getPlaceInfo(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("polygon_kml") polygonKml: Int = 1
    ): List<PlaceResponse>
}
