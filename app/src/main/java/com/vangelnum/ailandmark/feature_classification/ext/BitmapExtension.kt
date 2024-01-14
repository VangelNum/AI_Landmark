package com.vangelnum.ailandmark.feature_classification.ext

import android.graphics.Bitmap


fun Bitmap.centerCrop(desiredWidth: Int, desiredHeight: Int): Bitmap {
    val xStart = (width - desiredWidth) / 2
    val yStart = (height - desiredHeight) / 2

    if (xStart < 0 || yStart < 0 || desiredHeight > width || desiredWidth > height) {
        throw IllegalArgumentException("Invalid arguments for center cropping")
    }
    return Bitmap.createBitmap(this, xStart, yStart, desiredWidth, desiredHeight)
}