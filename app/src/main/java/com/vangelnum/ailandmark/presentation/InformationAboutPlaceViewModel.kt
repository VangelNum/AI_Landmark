package com.vangelnum.ailandmark.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vangelnum.ailandmark.data.PlaceResponse
import com.vangelnum.ailandmark.domain.OpenStreetMapService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InformationAboutPlaceViewModel : ViewModel() {

    private val _placeResponse = MutableStateFlow<List<PlaceResponse>?>(emptyList())
    val placeInfo = _placeResponse.asStateFlow()

    private val openStreetMapService: OpenStreetMapService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        openStreetMapService = retrofit.create(OpenStreetMapService::class.java)
    }

    fun getPlaceInfo(placeName: String) {
        println(placeName)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = openStreetMapService.getPlaceInfo(placeName)
                if (response.isSuccessful) {
                    val place = response.body()
                    _placeResponse.value = place
                } else {
                    Log.e("InformationAboutPlaceViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("InformationAboutPlaceViewModel", "Error: ${e.message}")
            }
        }
    }
}