package com.example.coroutinesplayground.koin

import com.example.coroutinesplayground.api.MoviesService
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    single { createRetrofit(get(), get()) }
    single { createService<MoviesService>(get()) }
}

fun createRetrofit(gson: Gson, client: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl("http://api.themoviedb.org")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

inline fun <reified T> createService(retrofit: Retrofit): T = retrofit.create(T::class.java)