package com.example.coroutinesplayground.movies

import com.example.coroutinesplayground.api.MoviesService
import com.example.coroutinesplayground.common.BaseError
import com.example.coroutinesplayground.common.NetworkError
import com.example.coroutinesplayground.createMovieDetails
import com.example.coroutinesplayground.createMoviesResponse
import com.example.coroutinesplayground.db.TokenDb
import com.example.coroutinesplayground.token.TokenDao
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
import io.kotlintest.assertions.arrow.either.shouldNotBeLeft
import io.kotlintest.matchers.string.shouldNotBeEmpty
import io.kotlintest.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.lang.Exception
import java.net.UnknownHostException

class MoviesDaoTest {
    private val service: MoviesService = mockk()
    private val tokenDb: TokenDb = mockk()

    private val tokenDao: TokenDao = TokenDao(tokenDb)
    private val propererApiToken = "111"

    private fun dao() = MoviesDao(tokenDao, service)

    @Test
    fun `test when token is correct and movies response is successful THEN return Right with correct size`() =
        runBlockingTest {
            coEvery { tokenDb.getToken() } returns propererApiToken
            coEvery { service.movieList(match { it == propererApiToken }) } returns createMoviesResponse(
                5
            )
            dao().moviesResponse.collect {
                it shouldBeRight { it.results.size shouldBe 5 }
            }
        }

    @Test
    fun `test when token is not correct AND API throws exception THEN return Left`() =
        runBlockingTest {
            coEvery { tokenDb.getToken() } returns "123"
            coEvery { service.movieList(match { it == propererApiToken }) } returns createMoviesResponse(5)
            dao().moviesResponse.collect {
                it.shouldBeLeft()
            }
        }

    @Test
    fun `test when token is correct BUT movies response fails THEN return Left`() =
        runBlockingTest {
            coEvery { tokenDb.getToken() } returns propererApiToken
            coEvery { service.movieList(match { it == propererApiToken }) } throws Exception()
            dao().moviesResponse.collect {
                it shouldBeLeft BaseError
            }
        }

    @Test
    fun `test when token is correct BUT there is no internet THEN return Left with NetworkError`() =
        runBlockingTest {
            coEvery { tokenDb.getToken() } returns propererApiToken
            coEvery { service.movieList(match { it == propererApiToken }) } throws UnknownHostException()
            dao().moviesResponse.collect {
                it shouldBeLeft NetworkError
            }
        }

    @Test
    fun `test when token is not correct AND there is no internet THEN return Left with other than NetworkError`() =
        runBlockingTest {
            coEvery { tokenDb.getToken() } returns "123"
            coEvery { service.movieList(match { it == propererApiToken }) } throws UnknownHostException()
            dao().moviesResponse.collect {
                it shouldNotBeLeft NetworkError
            }
        }

    @Test
    fun `test when movie details response is successful THEN return Right`() = runBlockingTest {
        val movieId = 111
        coEvery { tokenDb.getToken() } returns propererApiToken
        coEvery {
            service.movieDetails(
                match { it == movieId },
                propererApiToken
            )
        } coAnswers { createMovieDetails(movieId) }

        dao().movieDetailsResponse(movieId).collect {
            it shouldBeRight {
                it.id shouldBe movieId
                it.overview.shouldNotBeEmpty()
                it.title.shouldNotBeEmpty()
            }
        }
    }

    @Test
    fun `test when used non-existent movieId THEN details response returns Left`() = runBlockingTest {
        val movieId = 111
        coEvery { tokenDb.getToken() } returns propererApiToken
        coEvery {
            service.movieDetails(
                match { it == 123 },
                match { it == propererApiToken }
            )
        } coAnswers { createMovieDetails(123) }

        dao().movieDetailsResponse(movieId).collect {
            it.shouldBeLeft()
        }
    }

    @Test
    fun `test when token is not correct THEN details response returns Left`() = runBlockingTest {
        val movieId = 111
        coEvery { tokenDb.getToken() } returns "123"
        coEvery {
            service.movieDetails(
                match { it == movieId },
                match { it == propererApiToken }
            )
        } coAnswers { createMovieDetails(movieId) }

        dao().movieDetailsResponse(movieId).collect {
            it.shouldBeLeft()
        }
    }
}