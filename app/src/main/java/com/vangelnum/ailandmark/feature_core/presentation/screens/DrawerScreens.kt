package com.vangelnum.ailandmark.feature_core.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vangelnum.ailandmark.R

sealed class DrawerScreens(
    val route: String,
    val icon: @Composable (modifier: Modifier) -> Unit,
    val label: @Composable () -> Unit
) {
    object CameraScreen : DrawerScreens(
        route = Screens.CameraScreen.route,
        icon = { modifier ->
            Icon(
                imageVector = Icons.Outlined.Home,
                contentDescription = "main screen",
                modifier = modifier
            )
        },
        label = { Text(text = stringResource(id = R.string.main_screen)) }
    )

    object SearchHistoryScreen : DrawerScreens(
        route = Screens.SearchHistoryScreen.route,
        icon = { modifier ->
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = "search history",
                modifier = modifier
            )
        },
        label = { Text(text = stringResource(id = R.string.search_history)) }
    )
}