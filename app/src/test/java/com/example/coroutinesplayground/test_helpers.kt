package com.example.coroutinesplayground

import com.example.coroutinesplayground.models.Movie
import com.example.coroutinesplayground.models.MoviesResponse
import java.util.*

fun createMoviesResponse(size: Int = 1) = MoviesResponse(
    results = (0 until size).map { createMovie() }
)

private fun createMovie() = Movie(
    id = Random().nextInt(),
    title = UUID.randomUUID().toString()
)