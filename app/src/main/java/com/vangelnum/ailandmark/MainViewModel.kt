package com.vangelnum.ailandmark

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.vangelnum.ailandmark.feature_classification.data.ClassificationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _classificationList = MutableStateFlow(
        (ClassificationResult(
            emptyList(),
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        ))
    )
    val classificationList = _classificationList.asStateFlow()

    fun setValue(classificationList: ClassificationResult) {
        _classificationList.value = classificationList
    }
}