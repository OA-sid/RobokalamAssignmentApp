package com.example.robokalamassignmentapp.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class QuoteRepository private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context
        .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    suspend fun getQuoteOfTheDay(): QuoteResponse {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val lastFetchDate = Date(sharedPreferences.getLong(KEY_LAST_FETCH_DATE, 0))
        val cachedQuote = sharedPreferences.getString(KEY_CACHED_QUOTE, null)

        return if (cachedQuote != null && lastFetchDate >= today) {
            // Return cached quote if it's from today
            gson.fromJson(cachedQuote, QuoteResponse::class.java)
        } else {
            // Fetch new quote and cache it
            try {
                val newQuote = withContext(Dispatchers.IO) {
                    QuoteApiClient.service.getRandomQuote()
                }

                // Cache the new quote
                sharedPreferences.edit()
                    .putString(KEY_CACHED_QUOTE, gson.toJson(newQuote))
                    .putLong(KEY_LAST_FETCH_DATE, System.currentTimeMillis())
                    .apply()

                newQuote
            } catch (e: Exception) {
                // Fallback quote in case of API failure
                QuoteResponse(
                    "Success is not final, failure is not fatal: It is the courage to continue that counts.",
                    "Winston Churchill"
                )
            }
        }
    }

    companion object {
        private const val PREFERENCES_NAME = "quote_preferences"
        private const val KEY_CACHED_QUOTE = "cached_quote"
        private const val KEY_LAST_FETCH_DATE = "last_fetch_date"

        @Volatile
        private var INSTANCE: QuoteRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = QuoteRepository(context.applicationContext)
                }
            }
        }

        fun getInstance(): QuoteRepository {
            return INSTANCE ?: throw IllegalStateException(
                "QuoteRepository must be initialized before getting instance"
            )
        }
    }
}