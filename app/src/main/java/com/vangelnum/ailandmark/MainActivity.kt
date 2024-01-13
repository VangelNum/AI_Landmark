package com.vangelnum.ailandmark

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vangelnum.ailandmark.presentation.InformationAboutPlace
import com.vangelnum.ailandmark.presentation.InformationAboutPlaceViewModel
import com.vangelnum.ailandmark.presentation.InformationScreen
import com.vangelnum.ailandmark.presentation.MainScreen
import com.vangelnum.ailandmark.presentation.MainViewModel
import com.vangelnum.ailandmark.presentation.screens.Screens
import com.vangelnum.ailandmark.ui.theme.AILandmarkTheme
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasCameraPermission()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), 0
            )
        }
        if (!hasLocationPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                0
            )
        }
        MapKitFactory.setApiKey("74a45254-7bef-4167-916a-39bbef18987d")
        MapKitFactory.initialize(this)
        mapView = MapView(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            AILandmarkTheme {
                val navController = rememberNavController()
                val mainViewModel by viewModels<MainViewModel>()
                val classificationList = mainViewModel.classificationList.collectAsState()
                NavHost(
                    navController = navController,
                    startDestination = Screens.MainScreen.route
                ) {
                    composable(Screens.MainScreen.route) {
                        MainScreen(
                            applicationContext = applicationContext,
                            onNavigateToInformation = { classifications ->
                                mainViewModel.setValue(classifications)
                                navController.navigate(Screens.InformationScreen.route)
                            }
                        )
                    }
                    composable(Screens.InformationScreen.route) {
                        InformationScreen(
                            classificationList.value,
                            onNavigateToInformationAboutPlace = { place ->
                                navController.navigate("${Screens.InformationAboutPlace.route}/$place")
                            })
                    }
                    composable("${Screens.InformationAboutPlace.route}/{place}") { entry ->
                        val placeViewModel by viewModels<InformationAboutPlaceViewModel>()
                        val place = entry.arguments?.getString("place")
                        LaunchedEffect(key1 = Unit) {
                            placeViewModel.getPlaceInfo(place!!)
                        }
                        val placeInfo = placeViewModel.placeInfo.collectAsState()
                        InformationAboutPlace(state = placeInfo.value, fusedLocationClient)
                    }
                }
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Combine the results using logical AND
        return coarseLocationPermission && fineLocationPermission
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }
}