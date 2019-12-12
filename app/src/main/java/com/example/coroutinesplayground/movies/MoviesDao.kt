package com.example.coroutinesplayground.movies

import android.content.SharedPreferences
import arrow.core.Either
import arrow.core.Left
import com.example.coroutinesplayground.api.MoviesService
import com.example.coroutinesplayground.common.DefaultError
import com.example.coroutinesplayground.common.toDefaultError
import com.example.coroutinesplayground.token.TokenDao
import com.example.coroutinesplayground.models.MovieDetailsResponse
import com.example.coroutinesplayground.models.MoviesResponse
import com.example.coroutinesplayground.util.mapRight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class MoviesDao(
    tokenDao: TokenDao,
    private val service: MoviesService
) {

    val moviesResponse: Flow<Either<DefaultError, MoviesResponse>> = tokenDao.userFlow
        .mapRight {
            it.callWithToken { token ->
                service.movieList(token)
            }
        }
        .catch { emit(Left(it.toDefaultError())) }


    val movieDetailsResponse: (Int) -> Flow<Either<DefaultError, MovieDetailsResponse>> = { movieId ->
        tokenDao.userFlow.mapRight {
            it.callWithToken { token ->
                service.movieDetails(movieId, token)
            }
        }.catch { emit(Left(it.toDefaultError())) }
    }
}

fun SharedPreferences.string(
    defaultValue: String = "",
    key: String? = null
): ReadWriteProperty<Any, String?> {
    return object : ReadWriteProperty<Any, String?> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
            getString(key ?: property.name, defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) =
            edit().putString(key ?: property.name, value).apply()

    }
}