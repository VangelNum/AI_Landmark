package com.vangelnum.ailandmark.feature_core.presentation.screens

sealed class Screens(val route: String) {
    object CameraScreen : Screens("camera_screen")
    object ClassificationScreen : Screens("classification_screen")
    object LookupAboutPlace: Screens("lookup_place_information_screen")
}