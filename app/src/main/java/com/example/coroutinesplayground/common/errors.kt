package com.example.coroutinesplayground.common

import retrofit2.HttpException
import java.net.UnknownHostException

interface DefaultError

object EmptyBodyError : DefaultError
object RetrofitError : DefaultError
object ProgressError : DefaultError
object BaseError : DefaultError
object TokenError : DefaultError
object MovieIdError : DefaultError
object EmptyListError : DefaultError
object NetworkError : DefaultError

fun Throwable.toDefaultError(): DefaultError =
    when (this) {
        is HttpException -> RetrofitError
        is UnknownHostException -> NetworkError
        else -> BaseError
    }
