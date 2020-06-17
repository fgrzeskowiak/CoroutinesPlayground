package com.example.coroutinesplayground.db

import android.content.SharedPreferences
import kotlinx.coroutines.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TokenDb(
    preferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher
) {

    init {
        GlobalScope.launch {
            saveToken("474073102d23f5ddafb020004b4f61da")
        }
    }

    private var token by preferences.string()

    suspend fun getToken(): String? = withContext(ioDispatcher) {
        token
    }

    suspend fun saveToken(token: String) = withContext(ioDispatcher) {
        this@TokenDb.token = token
    }
}

private fun SharedPreferences.string(
    defaultValue: String = "",
    key: String? = null
): ReadWriteProperty<Any, String?> {
    return object : ReadWriteProperty<Any, String?> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
            getString(key ?: property.name, defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) =
            edit().putString(key ?: property.name, value).apply()

    }
}