package com.example.coroutinesplayground

import android.app.Application
import com.example.coroutinesplayground.koin.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(
                listOf(
                    networkModule,
                    retrofitModule,
                    tokenModule,
                    moviesModule,
                    basicModule
                )
            )
        }
    }
}