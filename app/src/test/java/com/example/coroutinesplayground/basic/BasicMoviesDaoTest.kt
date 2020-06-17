package com.example.coroutinesplayground.basic

import com.example.coroutinesplayground.api.MoviesService
import com.example.coroutinesplayground.common.BaseError
import com.example.coroutinesplayground.common.TokenError
import com.example.coroutinesplayground.createMoviesResponse
import com.example.coroutinesplayground.db.TokenDb
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class BasicMoviesDaoTest {
    private val service: MoviesService = mockk {
        coEvery { movieList(any()) } returns createMoviesResponse(5)
    }

    private val tokenDb: TokenDb = mockk {
        coEvery { getToken() } returns "111"
    }

    private fun dao() = BasicMoviesDao(service, tokenDb)

    @Test
    fun `test when success THEN movies are not empty`() = runBlockingTest {

        dao().getMovies() shouldBeRight { it.results.shouldNotBeEmpty() }
    }

    @Test
    fun `test when wrong token THEN request fails`() = runBlockingTest {
        coEvery { service.movieList(match { it != "111" }) } throws Exception()
        coEvery { tokenDb.getToken() } returns "123"

        dao().getMovies() shouldBeLeft BaseError
    }

    @Test
    fun `test when token in null THEN request fails`() = runBlockingTest {
        coEvery { tokenDb.getToken() } returns null

        dao().getMovies() shouldBeLeft TokenError
    }
}