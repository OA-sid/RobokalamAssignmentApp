package com.example.robokalamassignmentapp

import android.app.Application
import com.example.robokalamassignmentapp.data.QuoteRepository
import com.example.robokalamassignmentapp.data.UserPreferencesManager

class RobokalamApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize singletons
        UserPreferencesManager.initialize(this)
        QuoteRepository.initialize(this)
    }
}