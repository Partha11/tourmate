package com.twainarc.tourmate

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TourApp: Application(), Configuration.Provider {

    @Inject
    lateinit var factory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(factory)
        .setMinimumLoggingLevel(android.util.Log.INFO)
        .build()
}