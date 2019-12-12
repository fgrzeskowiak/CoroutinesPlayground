package com.example.coroutinesplayground.token

import arrow.core.Either
import arrow.core.toOption
import com.example.coroutinesplayground.common.DefaultError
import com.example.coroutinesplayground.common.TokenError
import com.example.coroutinesplayground.db.TokenDb
import kotlinx.coroutines.flow.*
import retrofit2.HttpException

class TokenDao(private val tokenDb: TokenDb) {

    val userFlow: Flow<Either<DefaultError, UserDao>> = flow { emit(tokenDb.getToken()) }
        .map { it.toOption().toEither { TokenError }.map { UserDao(it) } }

    inner class UserDao(private val token: String) {
        suspend fun <T> callWithToken(request: suspend (token: String) -> T): T {
            return try {
                request(token)
            } catch (exception: Exception) {
                if (exception is HttpException && exception.code() == 401) {
                    request(token)
                } else {
                    throw exception
                }
            }
        }
    }
}