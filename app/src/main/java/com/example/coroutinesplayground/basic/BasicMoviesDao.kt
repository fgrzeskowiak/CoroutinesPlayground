package com.example.coroutinesplayground.basic

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.Try
import com.example.coroutinesplayground.api.MoviesService
import com.example.coroutinesplayground.common.DefaultError
import com.example.coroutinesplayground.common.EmptyBodyError
import com.example.coroutinesplayground.common.toDefaultError
import com.example.coroutinesplayground.db.TokenDb
import com.example.coroutinesplayground.models.MoviesResponse
import com.example.coroutinesplayground.util.toEither
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class BasicMoviesDao(
    private val service: MoviesService,
    private val tokenDb: TokenDb
) {

    suspend fun getMovies(): Either<DefaultError, MoviesResponse> {
        val token = tokenDb.getToken()
        return token?.let {
                try {
                    Right(service.movieList(it))
                } catch (e: Exception) {
                    Left(e.toDefaultError())
                }
        } ?: Left(EmptyBodyError)
    }

//    suspend fun getMovies(): Either<DefaultError, MoviesResponse> {
//        val token = tokenDb.getToken()
//        return token?.let {
//            withContext(Dispatchers.IO) {
//                Try { service.movieList(it) }.toEither { it.toDefaultError() }
//            }
//        } ?: Left(EmptyBodyError)
//    }
//
//    WRONG!
//    suspend fun getMovies(): Either<DefaultError, MoviesResponse> {
//        val token = tokenDb.getToken()
//        return token?.let {
//            withContext(Dispatchers.IO) {
//                service.movieList(it)
//                    .toEither()
//            }
//        } ?: Left(EmptyBodyError)
//    }
}