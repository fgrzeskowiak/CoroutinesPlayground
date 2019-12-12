package com.example.coroutinesplayground.db

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TokenDb(preferences: SharedPreferences) {

    init {
        GlobalScope.launch {
            saveToken("474073102d23f5ddafb020004b4f61da")
        }
    }

    private var token by preferences.string()

    suspend fun getToken(): String? = withContext(Dispatchers.IO) {
        token
    }

    suspend fun saveToken(token: String) = withContext(Dispatchers.IO) {
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