package com.vangelnum.ailandmark.data

import android.graphics.Bitmap
import com.vangelnum.ailandmark.domain.Classification

data class ClassificationResult(
    val classifications: List<Classification>,
    val imageBitmap: Bitmap
)