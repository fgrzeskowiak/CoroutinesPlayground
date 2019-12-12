package com.example.coroutinesplayground.util

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import com.epam.coroutinecache.annotations.*
import com.epam.coroutinecache.api.ParameterizedDataProvider
import com.example.coroutinesplayground.common.DefaultError
import com.example.coroutinesplayground.common.EmptyBodyError
import com.example.coroutinesplayground.common.RetrofitError
import com.example.coroutinesplayground.common.toDefaultError
import com.example.coroutinesplayground.models.MovieDetailsResponse
import com.example.coroutinesplayground.models.MoviesResponse
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.util.concurrent.TimeUnit

fun <T> Response<T>.toEither(): Either<DefaultError, T> = try {
    when {
        body() == null -> Left(EmptyBodyError)
        isSuccessful -> Right(body()!!)
        else -> Left(RetrofitError)
    }
} catch (e: Exception) {
    Left(RetrofitError)
}

fun <T> T.toEither(): Either<DefaultError, T> = try {
    Right(this)
} catch (e: Exception) {
    Left(e.toDefaultError())
}

fun <T> Flow<T>.toEither(): Flow<Either<DefaultError, T>> =
    map<T, Either<DefaultError, T>> { Right(it) }
        .catch { emit(Left(it.toDefaultError())) }

interface CacheProviders {
    @ProviderKey("TestKey", EntryClass(Response::class))
    @LifeTime(value = 5, unit = TimeUnit.SECONDS)
    @Expirable
    @UseIfExpired
    suspend fun getData(provider: ParameterizedDataProvider<Response<MoviesResponse>>): Response<MoviesResponse>

    @ProviderKey("TestKey", EntryClass(Either::class))
    @LifeTime(value = 5, unit = TimeUnit.SECONDS)
    @Expirable
    @UseIfExpired
    suspend fun getDetailsData(provider: suspend () -> MovieDetailsResponse): MovieDetailsResponse
}