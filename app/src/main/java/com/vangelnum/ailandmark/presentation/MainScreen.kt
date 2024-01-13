package com.vangelnum.ailandmark.presentation

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.vangelnum.ailandmark.R
import com.vangelnum.ailandmark.data.feature_classification.ClassificationResult
import com.vangelnum.ailandmark.data.feature_classification.TfLandMarkClassifier

@Composable
fun MainScreen(
    applicationContext: Context,
    onNavigateToInformation: (ClassificationResult) -> Unit
) {
    var classificationResult by remember {
        mutableStateOf(
            ClassificationResult(
                emptyList(),
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            )
        )
    }
    val analyzer = remember {
        LandmarkImageAnalyzer(
            classifier = TfLandMarkClassifier(
                context = applicationContext
            ),
            onResult = { classifications, bitmap ->
                classificationResult = ClassificationResult(classifications, bitmap)
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
        if (classificationResult.classifications.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedButton(shape = RoundedCornerShape(12.dp), onClick = {
                    onNavigateToInformation(classificationResult)
                }) {
                    Text(
                        text = stringResource(id = R.string.define),
                        fontSize = 20.sp,
                        color = Color.Cyan
                    )
                }
                Spacer(modifier = Modifier.height(64.dp))
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
