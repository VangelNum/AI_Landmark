package com.vangelnum.ailandmark.feature_lookup_place.presentation

import com.yandex.mapkit.geometry.Point

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
