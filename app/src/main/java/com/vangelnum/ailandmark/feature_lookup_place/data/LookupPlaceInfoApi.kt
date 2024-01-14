package com.vangelnum.ailandmark.feature_lookup_place.data

import retrofit2.http.GET
import retrofit2.http.Query

interface LookupPlaceInfoApi {
    @GET("lookup")
    suspend fun getLookupPlaceInfo(
        @Query("osm_ids") id: String,
        @Query("format") format: String = "json",
        @Query("extratags") extratags: Int = 1,
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("polygon_kml") polygonKml: Int = 1
    ): List<LookupPlaceInfo>
}