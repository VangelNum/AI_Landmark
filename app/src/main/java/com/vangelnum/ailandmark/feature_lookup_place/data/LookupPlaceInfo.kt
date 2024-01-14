package com.vangelnum.ailandmark.feature_lookup_place.data

import com.google.gson.annotations.SerializedName

data class LookupPlaceInfo(
    @SerializedName("address") val address: AddressX,
    @SerializedName("addresstype") val addressType: String,
    @SerializedName("boundingbox") val boundingBox: List<String>,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("extratags") val extraTags: Extratags?,
    @SerializedName("importance") val importance: Double,
    @SerializedName("lat") val latitude: String,
    @SerializedName("licence") val license: String,
    @SerializedName("lon") val longitude: String,
    @SerializedName("name") val name: String,
    @SerializedName("osm_id") val osmId: Long,
    @SerializedName("osm_type") val osmType: String,
    @SerializedName("place_id") val placeId: Long,
    @SerializedName("place_rank") val placeRank: Long,
    @SerializedName("type") val type: String,
    @SerializedName("geokml") val geoKml: String
)