package com.vangelnum.ailandmark.feature_place_info.data

data class Address(
    val road: String?,
    val hamlet: String?,
    val town: String?,
    val village: String?,
    val city: String?,
    val state_district: String?,
    val state: String?,
    val postcode: String?,
    val country: String?,
    val country_code: String?
)