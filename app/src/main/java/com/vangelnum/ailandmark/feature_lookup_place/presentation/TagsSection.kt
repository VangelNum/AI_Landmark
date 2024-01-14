package com.vangelnum.ailandmark.feature_lookup_place.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.vangelnum.ailandmark.feature_lookup_place.data.Extratags

@Composable
fun TagsSection(tags: Extratags) {
    TagsItem("Architect", tags.architect)
    TagsItem("Architect Wikidata", tags.architectWikidata)
    TagsItem("Building", tags.building)
    TagsItem("Description", tags.description)
    TagsItem("Heritage", tags.heritage)
    TagsItem("Heritage Operator", tags.heritageOperator)
    TagsItem("Heritage Website", tags.heritageWebsite)
    TagsItem("Image", tags.image)
    TagsItem("Tourism", tags.tourism)
    TagsItem("Wheelchair", tags.wheelchair)
    TagsItem("Wikidata", tags.wikidata)
    TagsItem("Wikipedia", tags.wikipedia)
    TagsItem("Year of Construction", tags.yearOfConstruction)
    TagsItem("Website", tags.website)
    TagsItem("Opening Hours", tags.openingHours)
}

@Composable
fun TagsItem(title: String, value: String?) {
    if (value != null) {
        Text("$title: $value")
    }
}
