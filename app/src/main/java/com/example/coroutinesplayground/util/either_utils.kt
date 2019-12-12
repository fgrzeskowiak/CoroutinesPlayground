package com.example.coroutinesplayground.util

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

fun <L, R, TR> Flow<Either<L, R>>.mapRightWithEither(function: suspend (R) -> Either<L, TR>): Flow<Either<L, TR>> =
    this.map { it.mapRightWithEither(function) }

fun <L, R, TR> Flow<Either<L, R>>.mapRight(function: suspend (R) -> TR): Flow<Either<L, TR>> =
    this.map { it.fold({ Left(it) }, { Right(function(it)) }) }

fun <L, R, TR> Flow<Either<L, R>>.flatMapMergeRight(function: suspend (R) -> Flow<TR>): Flow<Either<L, TR>> =
    this.flatMapMerge {
        it.fold(
            { flowOf(Left(it)) },
            { function(it).map { Right((it)) } })
    }

fun <L, R, TR> Flow<Either<L, R>>.flatMapMergeRightWithEither(function: suspend (R) -> Flow<Either<L, TR>>): Flow<Either<L, TR>> =
    this.flatMapMerge {
        it.fold(
            { flowOf(Left(it)) },
            { function(it) })
    }

suspend fun <L, R, TR> Either<L, R>.mapRightWithEither(function: suspend (R) -> Either<L, TR>): Either<L, TR> =
    fold({ Left(it) }, { function(it) })
