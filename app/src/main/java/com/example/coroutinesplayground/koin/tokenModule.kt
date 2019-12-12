package com.example.coroutinesplayground.koin

import android.content.Context
import com.example.coroutinesplayground.token.TokenDao
import com.example.coroutinesplayground.db.TokenDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val tokenModule = module {
    single { TokenDb(getSharedPreferences(androidContext())) }
    single { TokenDao(get()) }
}

private fun getSharedPreferences(context: Context) =
    context.getSharedPreferences("coroutines_database", Context.MODE_PRIVATE)