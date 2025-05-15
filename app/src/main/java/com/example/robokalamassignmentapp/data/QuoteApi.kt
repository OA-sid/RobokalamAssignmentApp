package com.example.robokalamassignmentapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface QuoteApi {
    @GET("api/quotes/random")
    suspend fun getRandomQuote(): QuoteResponse
}

data class QuoteResponse(
    val quote: String,
    val author: String
)

object QuoteApiClient {
    private const val BASE_URL = "https://zenquotes.io/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: QuoteApi = retrofit.create(QuoteApi::class.java)
}