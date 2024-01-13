package com.vangelnum.ailandmark.presentation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.vangelnum.ailandmark.data.feature_lookup.AddressX
import com.vangelnum.ailandmark.data.feature_lookup.Extratags
import com.vangelnum.ailandmark.data.feature_lookup.LookupPlaceInfo
import com.vangelnum.ailandmark.helpers.Resource
import com.yandex.mapkit.geometry.LinearRing
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

@Composable
fun InformationAboutPlace(
    state: Resource<List<LookupPlaceInfo>>,
    fusedLocationClient: FusedLocationProviderClient
) {
    when (state) {
        Resource.Empty -> {}
        is Resource.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message)
            }
        }

        Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            if (state.data.isNotEmpty()) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(state.data) { place ->
                        PlaceCard(place, fusedLocationClient)
                    }
                    item {
                        val license = state.data.first().license
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "License", textAlign = TextAlign.Center)
                            Text(text = license, textAlign = TextAlign.Center)
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No results found")
                }
            }
        }
    }
}

@Composable
fun PlaceCard(
    place: LookupPlaceInfo,
    fusedLocationClient: FusedLocationProviderClient
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEDDDF)),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = place.name)
            Text(text = "Type: ${place.type}")
            Text(
                text = "Address Type: ${place.addressType}"
            )
            Text(text = "Latitude: ${place.latitude}")
            Text(
                text = "Longitude: ${place.longitude}",
            )
            place.address.let { address ->
                AddressSection(address = address)
            }
            place.extraTags?.let { TagsSection(tags = it) }
            Spacer(modifier = Modifier.height(8.dp))
            MapSection(place = place)
        }
    }
}

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


fun parseGeokmlCoordinates(geokml: String): List<Point> {
    val coordinates = geokml
        .substringAfter("<coordinates>")
        .substringBefore("</coordinates>")
        .split(" ")
        .map {
            val (lon, lat) = it.split(",").map { coordinate -> coordinate.toDouble() }
            Point(lat, lon)
        }

    return coordinates
}

@Composable
fun MapSection(
    place: LookupPlaceInfo
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.height(250.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { MapView(it) }
        ) { mapView ->
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    0
                )
            }
            mapView.map.move(
                CameraPosition(
                    Point(place.latitude.toDouble(), place.longitude.toDouble()),
                    16.0f,
                    150.0f,
                    30.0f
                )
            )
            val polygonGeokml = createPolygon(place.geoKml)
            mapView.map.mapObjects.addPolygon(polygonGeokml)

            if (polygonGeokml.outerRing.points.isEmpty()) {
                val boundingBox = place.boundingBox

                val lat1 = boundingBox[0].toDouble()
                val lon1 = boundingBox[2].toDouble()
                val lat2 = boundingBox[1].toDouble()
                val lon2 = boundingBox[3].toDouble()

                val points = listOf(
                    Point(lat1, lon1),
                    Point(lat2, lon1),
                    Point(lat2, lon2),
                    Point(lat1, lon2)
                )

                val linearRing = LinearRing(points)
                val polygon = Polygon(linearRing, emptyList())
                mapView.map.mapObjects.addPolygon(polygon)
            }
        }
    }
}

fun createPolygon(geokml: String): Polygon {
    val coordinates = parseGeokmlCoordinates(geokml)
    val linearRing = LinearRing(coordinates)
    return Polygon(linearRing, emptyList())
}

@Composable
fun AddressSection(address: AddressX) {
    Column {
        Text(
            "Address"
        )
        AddressItem("Road", address.road)
        AddressItem("City", address.city)
        AddressItem("State", address.state)
        AddressItem("Postcode", address.postcode)
        AddressItem("Country", address.country)
        AddressItem("Country Code", address.countryCode)
    }
}

@Composable
fun AddressItem(title: String, value: String?) {
    if (value != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title)
            Text(value)
        }
    }
}