package com.vangelnum.ailandmark.presentation.screens

sealed class Screens(val route: String) {
    object MainScreen : Screens("main_screen")
    object InformationScreen : Screens("info_screen")
}