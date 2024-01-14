package com.vangelnum.ailandmark.feature_lookup_place.data

import com.google.gson.annotations.SerializedName

data class AddressX(
    @SerializedName("city") val city: String,
    @SerializedName("country") val country: String,
    @SerializedName("country_code") val countryCode: String,
    @SerializedName("historic") val historic: String,
    @SerializedName("postcode") val postcode: String,
    @SerializedName("road") val road: String,
    @SerializedName("state") val state: String,
    @SerializedName("suburb") val suburb: String
)