package com.vangelnum.ailandmark.data.feature_lookup

import com.google.gson.annotations.SerializedName

data class Extratags(
    @SerializedName("architect") val architect: String,
    @SerializedName("architectWikidata") val architectWikidata: String,
    @SerializedName("building") val building: String,
    @SerializedName("description") val description: String,
    @SerializedName("heritage") val heritage: String,
    @SerializedName("heritageOperator") val heritageOperator: String,
    @SerializedName("heritageWebsite") val heritageWebsite: String,
    @SerializedName("image") val image: String,
    @SerializedName("tourism") val tourism: String,
    @SerializedName("wheelchair") val wheelchair: String,
    @SerializedName("wikidata") val wikidata: String,
    @SerializedName("wikipedia") val wikipedia: String,
    @SerializedName("yearOfConstruction") val yearOfConstruction: String,
    @SerializedName("website") val website: String,
    @SerializedName("opening_hours") val openingHours: String
)