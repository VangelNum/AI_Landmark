package com.vangelnum.ailandmark.presentation

import android.content.Context
import android.view.ContextMenu
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
import androidx.core.content.ContextCompat
import com.vangelnum.ailandmark.R
import com.vangelnum.ailandmark.data.TfLandMarkClassifier
import com.vangelnum.ailandmark.domain.Classification

@Composable
fun MainScreen(
    applicationContext: Context,
    onNavigateToInformation:(List<Classification>) -> Unit
) {
    var classification by remember {
        mutableStateOf(emptyList<Classification>())
    }
    val analyzer = remember {
        LandmarkImageAnalyzer(
            classifier = TfLandMarkClassifier(
                context = applicationContext
            ),
            onResult = {
                classification = it
            }
        )
    }
    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(applicationContext),
                analyzer
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(controller = controller, modifier = Modifier.fillMaxSize())
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            PreviewCenterCropping(modifier = Modifier.aspectRatio(1f))
        }
        if (classification.isNotEmpty()) {
            OutlinedButton(onClick = {
                onNavigateToInformation(classification)
            }) {
                Text(text = stringResource(id = R.string.define))
            }
        }
    }
}

@Composable
fun PreviewCenterCropping(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val canvasHeight = size.height
        val canvasWidth = size.width
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = 0f, y = canvasHeight),
            color = Color.Green,
            strokeWidth = 4f,
            pathEffect = pathEffect
        )

        drawLine(
            start = Offset(x = 0f, y = canvasHeight),
            end = Offset(x = canvasWidth, y = canvasHeight),
            color = Color.Green,
            strokeWidth = 4f,
            pathEffect = pathEffect
        )

        drawLine(
            start = Offset(x = canvasWidth, y = canvasHeight),
            end = Offset(x = canvasWidth, y = 0f),
            color = Color.Green,
            strokeWidth = 4f,
            pathEffect = pathEffect
        )

        drawLine(
            start = Offset(x = canvasWidth, y = 0f),
            end = Offset(x = 0f, y = 0f),
            color = Color.Green,
            strokeWidth = 4f,
            pathEffect = pathEffect
        )
    }
}
