package com.vangelnum.ailandmark.feature_classification.data

import android.graphics.Bitmap
import com.vangelnum.ailandmark.feature_classification.domain.Classification

data class ClassificationResult(
    val classifications: List<Classification>,
    val imageBitmap: Bitmap
)