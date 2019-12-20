package com.example.coroutinesplayground

import TestCoroutineRule
import com.example.coroutinesplayground.models.Movie
import com.example.coroutinesplayground.models.MovieDetailsResponse
import com.example.coroutinesplayground.models.MoviesResponse
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import java.util.*

fun createMoviesResponse(size: Int = 1) = MoviesResponse(
    results = (0 until size).map { createMovie() }
)

private fun createMovie() = Movie(
    id = Random().nextInt(),
    title = UUID.randomUUID().toString()
)

fun createMovieDetails(id: Int) = MovieDetailsResponse(
    id = id,
    title = UUID.randomUUID().toString(),
    overview = UUID.randomUUID().toString()
)

fun TestCoroutineRule.runBlockingTest(testBody: suspend TestCoroutineScope.() -> Unit) {
    dispatcher.runBlockingTest(testBody)
}