package com.example.coroutinesplayground.koin

import arrow.core.Option
import com.example.coroutinesplayground.common.BaseAdapter
import com.example.coroutinesplayground.movies.MovieHolderManager
import com.example.coroutinesplayground.details.MovieDetailsPresenter
import com.example.coroutinesplayground.movies.MoviesActivity
import com.example.coroutinesplayground.movies.MoviesDao
import com.example.coroutinesplayground.movies.MoviesPresenter
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val moviesModule = module {
    single(named("cacheDir")) { androidContext().cacheDir }
    single { MoviesDao(get(), get()) }
    scope(named<MoviesActivity>()) {
        scoped { (lifecycleScope: CoroutineScope) ->
            BaseAdapter(
                listOf(
                    MovieHolderManager(lifecycleScope)
                )
            )
        }
    }
    viewModel { MoviesPresenter(get()) }
    viewModel { (movieId: Option<Int>) -> MovieDetailsPresenter(movieId, get()) }
}