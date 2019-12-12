package com.example.coroutinesplayground.koin

import com.example.coroutinesplayground.basic.BasicMoviesActivity
import com.example.coroutinesplayground.basic.BasicMoviesDao
import com.example.coroutinesplayground.basic.BasicMoviesPresenter
import com.example.coroutinesplayground.common.BaseAdapter
import com.example.coroutinesplayground.movies.MovieHolderManager
import kotlinx.coroutines.CoroutineScope
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val basicModule = module {
    single { BasicMoviesDao(get(), get()) }
    viewModel { BasicMoviesPresenter(get()) }
    scope(named<BasicMoviesActivity>()) {
        scoped { (lifecycleScope: CoroutineScope) ->
            BaseAdapter(
                listOf(MovieHolderManager(lifecycleScope))
            )
        }
    }
}