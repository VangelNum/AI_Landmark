package com.vangelnum.ailandmark

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vangelnum.ailandmark.data.TfLandMarkClassifier
import com.vangelnum.ailandmark.domain.Classification
import com.vangelnum.ailandmark.presentation.CameraPreview
import com.vangelnum.ailandmark.presentation.InformationScreen
import com.vangelnum.ailandmark.presentation.LandmarkImageAnalyzer
import com.vangelnum.ailandmark.presentation.MainScreen
import com.vangelnum.ailandmark.presentation.screens.Screens
import com.vangelnum.ailandmark.ui.theme.AILandmarkTheme
import java.io.Serializable

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
                NavHost(navController = navController, startDestination = Screens.MainScreen.route) {
                    composable(Screens.MainScreen.route) {
                        MainScreen(
                            applicationContext = applicationContext,
                            onNavigateToInformation = { classifications ->
                                navController.navigate("${Screens.InformationScreen.route}/$classifications")
                            }
                        )
                    }
                    composable("${Screens.InformationScreen.route}/{classifications}") { backStackEntry ->
                        val classifications = backStackEntry.arguments?.getSerializable("classifications") as List<Classification>
                        InformationScreen(classifications)
                    }
                }
            }
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}