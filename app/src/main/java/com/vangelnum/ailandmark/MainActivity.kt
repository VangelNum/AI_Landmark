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
import com.vangelnum.ailandmark.feature_classification.presentation.CameraScreen
import com.vangelnum.ailandmark.feature_classification.presentation.InformationScreen
import com.vangelnum.ailandmark.feature_core.presentation.screens.Screens
import com.vangelnum.ailandmark.feature_lookup_place.presentation.LookupAboutPlace
import com.vangelnum.ailandmark.feature_lookup_place.presentation.LookupAboutPlaceViewModel
import com.vangelnum.ailandmark.ui.theme.AILandmarkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
        setContent {
            AILandmarkTheme {
                val navController = rememberNavController()
                val mainViewModel by viewModels<MainViewModel>()
                val classificationList = mainViewModel.classificationList.collectAsState()
                NavHost(
                    navController = navController,
                    startDestination = Screens.CameraScreen.route
                ) {
                    composable(Screens.CameraScreen.route) {
                        CameraScreen(
                            applicationContext = applicationContext,
                            onNavigateToInformation = { classifications ->
                                mainViewModel.setValue(classifications)
                                navController.navigate(Screens.ClassificationScreen.route)
                            }
                        )
                    }
                    composable(Screens.ClassificationScreen.route) {
                        InformationScreen(
                            classificationList.value,
                            onNavigateToInformationAboutPlace = { place ->
                                navController.navigate("${Screens.LookupAboutPlace.route}/$place")
                            })
                    }
                    composable("${Screens.LookupAboutPlace.route}/{place}") { entry ->
                        val placeViewModel by viewModels<LookupAboutPlaceViewModel>()
                        val place = entry.arguments?.getString("place")
                        LaunchedEffect(key1 = Unit) {
                            placeViewModel.getListOfPlacesWithName(place!!)
                        }
                        val placeInfo = placeViewModel.allPlaceInformation.collectAsState()
                        LookupAboutPlace(state = placeInfo.value)
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

        return coarseLocationPermission && fineLocationPermission
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}