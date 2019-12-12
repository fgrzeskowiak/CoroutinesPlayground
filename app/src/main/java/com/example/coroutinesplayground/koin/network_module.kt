package com.example.coroutinesplayground.koin

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Loggable
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

val networkModule = module {
    single { cache(androidContext()) }
    single { loggingInterceptor() }
    single { createOkkHttpClient(get(), get()) }
    single { createGson() }
}


internal fun cache(context: Context): Cache {
    val httpCacheDir = File(context.cacheDir, "cache")
    val httpCacheSize = (150 * 1024 * 1024).toLong() // 150 MiB
    return Cache(httpCacheDir, httpCacheSize)
}

internal fun createOkkHttpClient(cache: Cache, loggingInterceptor: Interceptor): OkHttpClient {

    val builder = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build()
            )
        }
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })

    return builder.build()
}

fun loggingInterceptor(): Interceptor = CurlInterceptor(CurlLoggerLoggable())

class CurlLoggerLoggable : Loggable {

    override fun log(s: String) {
        println("" + s)
        LOGGER.log(Level.FINE, s)
    }

    companion object {
        private val LOGGER = Logger.getLogger("Curl")
    }
}

internal fun createGson(): Gson {
    return GsonBuilder()
        .create()
}