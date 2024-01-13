package com.vangelnum.ailandmark.data.feature_place_info

import com.google.gson.annotations.SerializedName


data class PlaceResponse(
    val address: Address?,
    @SerializedName("address_type")
    val addressType: String?,
    val boundingbox: List<String>,
    @SerializedName("class")
    val responseClass: String,
    @SerializedName("display_name")
    val displayName: String,
    val geokml: String,
    val importance: Double,
    val lat: String,
    val licence: String,
    val lon: String,
    val name: String,
    @SerializedName("osm_id")
    val osmId: Long,
    @SerializedName("osm_type")
    val osmType: String,
    @SerializedName("place_id")
    val placeId: Int,
    @SerializedName("place_rank")
    val placeRank: Int,
    val type: String
)