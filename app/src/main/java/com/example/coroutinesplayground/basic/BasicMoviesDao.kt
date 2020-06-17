package com.example.coroutinesplayground.basic

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import com.example.coroutinesplayground.api.MoviesService
import com.example.coroutinesplayground.common.DefaultError
import com.example.coroutinesplayground.common.TokenError
import com.example.coroutinesplayground.common.toDefaultError
import com.example.coroutinesplayground.db.TokenDb
import com.example.coroutinesplayground.models.MoviesResponse

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
        } ?: Left(TokenError)
    }

//    suspend fun getMovies(): Either<DefaultError, MoviesResponse> {
//        val token = tokenDb.getToken()
//        return token?.let {
//                Try { service.movieList(it) }.toEither { it.toDefaultError() }
//        } ?: Left(TokenError)
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
//        } ?: Left(TokenError)
//    }
}