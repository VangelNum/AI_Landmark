package com.vangelnum.ailandmark

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vangelnum.ailandmark.presentation.InformationAboutPlace
import com.vangelnum.ailandmark.presentation.InformationAboutPlaceViewModel
import com.vangelnum.ailandmark.presentation.InformationScreen
import com.vangelnum.ailandmark.presentation.MainScreen
import com.vangelnum.ailandmark.presentation.MainViewModel
import com.vangelnum.ailandmark.presentation.screens.Screens
import com.vangelnum.ailandmark.ui.theme.AILandmarkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasCameraPermission()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), 0
            )
        }
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
                        InformationScreen(classificationList.value, onNavigateToInformationAboutPlace = { place ->
                            navController.navigate("${Screens.InformationAboutPlace.route}/$place")
                        })
                    }
                    composable("${Screens.InformationAboutPlace.route}/{place}") { entry ->
                        val placeViewModel by viewModels<InformationAboutPlaceViewModel>()
                        val place = entry.arguments?.getString("place")
                        placeViewModel.getPlaceInfo(place!!)
                        val placeInfo = placeViewModel.placeInfo.collectAsState()
                        if (placeInfo.value?.isEmpty() == true) {
                           Text(text = "No information about this place")
                        } else {
                            InformationAboutPlace(placeInfo.value!!)
                        }
                    }
                }
            }
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}