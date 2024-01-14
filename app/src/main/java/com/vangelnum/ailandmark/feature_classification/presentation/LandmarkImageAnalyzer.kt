package com.vangelnum.ailandmark.feature_classification.presentation

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.vangelnum.ailandmark.feature_classification.domain.Classification
import com.vangelnum.ailandmark.feature_classification.domain.LandmarkClassifier
import com.vangelnum.ailandmark.feature_classification.ext.centerCrop

class LandmarkImageAnalyzer(
    private val classifier: LandmarkClassifier,
    private val onResult: (List<Classification>, Bitmap) -> Unit
) : ImageAnalysis.Analyzer {

    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {
        if (frameSkipCounter % 60 == 0) {
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image.toBitmap().centerCrop(321, 321)

            val rotatedBitmap = rotateBitmap(bitmap, rotationDegrees)

            val results = classifier.classify(rotatedBitmap, rotationDegrees)
            onResult(results, rotatedBitmap)
        }
        frameSkipCounter++

        image.close()
    }
}

private fun rotateBitmap(bitmap: Bitmap, rotation: Int): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(rotation.toFloat())
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
