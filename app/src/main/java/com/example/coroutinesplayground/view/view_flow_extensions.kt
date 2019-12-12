package com.example.coroutinesplayground.view

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.appcompat.widget.Toolbar
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@MainThread
fun TextView.textChanges(): Flow<String> = callbackFlow {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(text: Editable?) {
            if (!isClosedForSend) offer(text.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    }

    addTextChangedListener(textWatcher)
    awaitClose {
        removeTextChangedListener(textWatcher)
    }
}

@MainThread
fun View.clicks(): Flow<Unit> = callbackFlow {
    setOnClickListener { offer(Unit) }
    awaitClose {
        setOnClickListener(null)
    }
}

@MainThread
fun Toolbar.navigationClicks(): Flow<Unit> = callbackFlow {
    setNavigationOnClickListener { offer(Unit) }
    awaitClose { setNavigationOnClickListener(null) }

}