package com.example.coroutinesplayground.view

fun <T> List<T>.onEmpty(action:(List<T>) -> List<T>): List<T> = if(isEmpty()) action(this) else this