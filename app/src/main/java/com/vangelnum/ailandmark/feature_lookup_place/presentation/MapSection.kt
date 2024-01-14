package com.vangelnum.ailandmark.feature_lookup_place.presentation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vangelnum.ailandmark.R
import com.vangelnum.ailandmark.feature_core.helpers.ComposableLifecycle
import com.vangelnum.ailandmark.feature_lookup_place.data.LookupPlaceInfo
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.LinearRing
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

@Composable
fun MapSection(
    place: LookupPlaceInfo
) {
    val mapViews = remember { mutableListOf<MapView>() }
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val drivingRouter =
        DirectionsFactory.getInstance().createDrivingRouter()
    val drivingOptions = DrivingOptions().apply {
        routesCount = 1
    }
    val vehicleOptions = VehicleOptions()

    var mapView: MapView? = null

    ComposableLifecycle { _, event ->
        when (event) {

            Lifecycle.Event.ON_STOP -> {
                MapKitFactory.getInstance().onStop()
                mapViews.forEach { mapView ->
                    mapView.onStop()
                }
            }

            Lifecycle.Event.ON_START -> {
                MapKitFactory.getInstance().onStart()
                mapViews.forEach { mapView ->
                    mapView.onStart()
                }
            }

            else -> {

            }
        }
    }
    var userLatitude by remember {
        mutableDoubleStateOf(0.0)
    }
    var userLongitude by remember {
        mutableDoubleStateOf(0.0)
    }

    val cameraCallback = Map.CameraCallback {
        // Handle camera move finished ...
    }

    Card(
        modifier = Modifier.height(250.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    mapView = MapView(context)
                    mapViews.add(mapView!!)
                    mapView!!
                }
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
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    @Suppress("DEPRECATION")
                    mapView.mapWindow.map.mapObjects.addPlacemark(
                        Point(
                            location.latitude,
                            location.longitude
                        )
                    ).apply {
                        setIcon(ImageProvider.fromResource(context, R.drawable.my_location))
                    }
                    userLongitude = location.longitude
                    userLatitude = location.latitude
                }

                mapView.mapWindow.map.move(
                    CameraPosition(
                        Point(place.latitude.toDouble(), place.longitude.toDouble()),
                        16.0f,
                        150.0f,
                        30.0f
                    ),
                    Animation(Animation.Type.LINEAR, 1f),
                    cameraCallback
                )
                @Suppress("DEPRECATION")
                mapView.mapWindow.map.mapObjects.addPlacemark(
                    Point(
                        place.latitude.toDouble(),
                        place.longitude.toDouble()
                    )
                ).apply {
                    setIcon(ImageProvider.fromResource(context, R.drawable.location_on))
                }

                val polygonGeokml = createPolygon(place.geoKml)
                mapView.mapWindow.map.mapObjects.addPolygon(polygonGeokml)

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
                    mapView.mapWindow.map.mapObjects.addPolygon(polygon)
                }
            }
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HelperNavigationIcon(imageVector = Icons.Outlined.MyLocation) {
                    mapView?.mapWindow?.map?.move(
                        CameraPosition(
                            Point(userLatitude, userLongitude),
                            16.0f,
                            150.0f,
                            30.0f
                        ),
                        Animation(Animation.Type.LINEAR, 1f),
                        cameraCallback
                    )
                }
                HelperNavigationIcon(imageVector = Icons.Outlined.LocationOn) {
                    mapView?.mapWindow?.map?.move(
                        CameraPosition(
                            Point(place.latitude.toDouble(), place.longitude.toDouble()),
                            16.0f,
                            150.0f,
                            30.0f
                        ),
                        Animation(Animation.Type.LINEAR, 1f),
                        cameraCallback
                    )
                }
                var drivingSession by remember {
                    mutableStateOf<DrivingSession?>(null)
                }
                HelperNavigationIcon(imageVector = Icons.Outlined.Navigation, onClick = {
                    drivingSession?.cancel()
                    val points = buildList {
                        add(
                            RequestPoint(
                                Point(
                                    place.latitude.toDouble(),
                                    place.longitude.toDouble()
                                ), RequestPointType.WAYPOINT, null, null
                            )
                        )
                        add(
                            RequestPoint(
                                Point(userLatitude, userLongitude),
                                RequestPointType.WAYPOINT,
                                null, null
                            )
                        )
                    }

                    val drivingRouteListener = object : DrivingSession.DrivingRouteListener {
                        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
                            Log.d("MapSection", "Received ${drivingRoutes.size} driving routes")
                            if (drivingRoutes.isNotEmpty()) {
                                val geometry = drivingRoutes[0].geometry
                                val polyline = Polyline(
                                    geometry.points.map { Point(it.latitude, it.longitude) }
                                )
                                mapView?.mapWindow?.map?.mapObjects?.addPolyline(polyline)
                                val geometryNew = Geometry.fromPolyline(polyline)
                                val position = mapView?.mapWindow?.map?.cameraPosition(
                                    geometryNew
                                )
                                if (position != null) {
                                    mapView?.mapWindow?.map?.move(
                                        position
                                    )
                                }
                            } else {
                                Log.e("MapSection", "No driving routes received")
                            }
                        }

                        override fun onDrivingRoutesError(error: com.yandex.runtime.Error) {
                            Log.e("MapSection", "Error in driving routes calculation: $error")
                        }
                    }
                    drivingSession = drivingRouter.requestRoutes(
                        points,
                        drivingOptions,
                        vehicleOptions,
                        drivingRouteListener
                    )
                })
            }
        }
    }
}

@Composable
fun HelperNavigationIcon(
    imageVector: ImageVector,
    contentDescription: String = "",
    onClick: () -> Unit
) {
    FilledTonalIconButton(onClick = onClick) {
        Icon(imageVector, contentDescription = contentDescription)
    }
}

fun createPolygon(geokml: String): Polygon {
    val coordinates = parseGeokmlCoordinates(geokml)
    val linearRing = LinearRing(coordinates)
    return Polygon(linearRing, emptyList())
}

