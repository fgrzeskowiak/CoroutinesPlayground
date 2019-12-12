package com.example.coroutinesplayground.common

import android.content.res.Resources
import com.example.coroutinesplayground.R

internal fun DefaultError.toMessage(resources: Resources): String = when(this) {
    RetrofitError -> resources.getString(R.string.retrofit_error)
    EmptyListError -> resources.getString(R.string.empty_list_error)
    NetworkError -> resources.getString(R.string.network_error)
    else -> resources.getString(R.string.base_error)
}