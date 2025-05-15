package com.example.robokalamassignmentapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreferencesManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context
        .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveLoginStatus(email: String, name: String) {
        sharedPreferences.edit {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_NAME, name)
        }
    }

    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUserName(): String = sharedPreferences.getString(KEY_USER_NAME, "") ?: ""

    fun getUserEmail(): String = sharedPreferences.getString(KEY_USER_EMAIL, "") ?: ""

    fun clearLoginStatus() {
        sharedPreferences.edit {
            putBoolean(KEY_IS_LOGGED_IN, false)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_NAME)
        }
    }

    companion object {
        private const val PREFERENCES_NAME = "robokalam_preferences"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME = "user_name"

        @Volatile
        private var INSTANCE: UserPreferencesManager? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = UserPreferencesManager(context.applicationContext)
                }
            }
        }

        fun getInstance(): UserPreferencesManager {
            return INSTANCE ?: throw IllegalStateException(
                "UserPreferencesManager must be initialized before getting instance"
            )
        }
    }
}
