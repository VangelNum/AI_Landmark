package com.vangelnum.ailandmark

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vangelnum.ailandmark.feature_classification.presentation.CameraScreen
import com.vangelnum.ailandmark.feature_classification.presentation.InformationScreen
import com.vangelnum.ailandmark.feature_core.presentation.screens.DrawerScreens
import com.vangelnum.ailandmark.feature_core.presentation.screens.Screens
import com.vangelnum.ailandmark.feature_history_search.presentation.SearchHistoryScreen
import com.vangelnum.ailandmark.feature_history_search.presentation.SearchHistoryViewModel
import com.vangelnum.ailandmark.feature_lookup_place.presentation.LookupAboutPlace
import com.vangelnum.ailandmark.feature_lookup_place.presentation.LookupAboutPlaceViewModel
import com.vangelnum.ailandmark.ui.theme.AILandmarkTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
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
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                val mainViewModel by viewModels<MainViewModel>()
                val classificationList = mainViewModel.classificationList.collectAsState()
                val searchHistoryViewModel by viewModels<SearchHistoryViewModel>()
                val searchState = searchHistoryViewModel.searchHistoryState.collectAsState()
                ModalNavigationDrawer(
                    gesturesEnabled = !drawerState.isClosed,
                    drawerState = drawerState,
                    drawerContent = {
                        val currentRoute =
                            navController.currentBackStackEntryAsState().value?.destination?.route
                        ModalDrawerSheet {
                            val drawerScreens = listOf(
                                DrawerScreens.CameraScreen,
                                DrawerScreens.SearchHistoryScreen,
                            )
                            NavigationDrawerItem(
                                label = {
                                    Text(text = "Close")
                                },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Close,
                                        contentDescription = "close"
                                    )
                                }
                            )

                            for (drawerScreen in drawerScreens) {
                                NavigationDrawerItem(
                                    icon = {
                                        drawerScreen.icon(Modifier)
                                    },
                                    label = {
                                        drawerScreen.label()
                                    },
                                    selected = currentRoute == drawerScreen.route,
                                    onClick = {
                                        scope.launch {
                                            drawerState.close()
                                        }
                                        if (currentRoute != drawerScreen.route) {
                                            navController.navigate(drawerScreen.route) {
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }) {
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(title = {
                                Text(text = "AI LANDMARK")
                            }, navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Menu,
                                        contentDescription = "menu"
                                    )
                                }
                            })
                        }
                    ) { contentPadding ->
                        NavHost(
                            modifier = Modifier.padding(contentPadding),
                            navController = navController,
                            startDestination = Screens.CameraScreen.route,
                            enterTransition = {
                                EnterTransition.None
                            },
                            exitTransition = {
                                ExitTransition.None
                            }
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
                                    information = classificationList.value,
                                    onNavigateToInformationAboutPlace = { place ->
                                        navController.navigate("${Screens.LookupAboutPlaceScreen.route}/$place")
                                    },
                                    insertToSearchHistory = {
                                        searchHistoryViewModel.insertToSearchHistory(it)
                                    }
                                )
                            }
                            composable("${Screens.LookupAboutPlaceScreen.route}/{place}") { entry ->
                                val placeViewModel by viewModels<LookupAboutPlaceViewModel>()
                                val place = entry.arguments?.getString("place")
                                LaunchedEffect(key1 = Unit) {
                                    placeViewModel.getListOfPlacesWithName(place!!)
                                }
                                val placeInfo = placeViewModel.allPlaceInformation.collectAsState()
                                LookupAboutPlace(state = placeInfo.value)
                            }
                            composable(Screens.SearchHistoryScreen.route) {
                                SearchHistoryScreen(
                                    searchState,
                                    onNavigateToInformationAboutPlace = {
                                        navController.navigate("${Screens.LookupAboutPlaceScreen.route}/$it")
                                    },
                                    onDelete = {
                                        searchHistoryViewModel.deleteFromSearchHistory(it)
                                    }
                                )
                            }
                        }
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