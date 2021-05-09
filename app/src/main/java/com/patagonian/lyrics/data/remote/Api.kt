package com.patagonian.lyrics.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {
    private const val BASE_URL = "https://api.lyrics.ovh/v1/"

    fun create(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}