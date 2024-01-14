package com.vangelnum.ailandmark

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("74a45254-7bef-4167-916a-39bbef18987d")
        MapKitFactory.initialize(this)
    }
}