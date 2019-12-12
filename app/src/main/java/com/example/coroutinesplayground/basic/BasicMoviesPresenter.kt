package com.example.coroutinesplayground.basic

import androidx.lifecycle.*
import com.example.coroutinesplayground.common.MoviesState
import com.example.coroutinesplayground.common.ScreenState
import com.example.coroutinesplayground.movies.MovieAdapterItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class BasicMoviesPresenter(basicMoviesDao: BasicMoviesDao) : ViewModel() {

    private val moviesData = MutableLiveData<ScreenState>()

    val channel = Channel<String>()

    init {

        moviesData.postValue(ScreenState.Init)

        viewModelScope.launch {
            val movieItems = basicMoviesDao.getMovies()
                .map {
                    it.results.map {
                        MovieAdapterItem(
                            it.id,
                            it.title,
                            MediatorLiveData()
                        )
                    }
                }

            moviesData.postValue(
                movieItems.fold(
                    { ScreenState.Failed(it) },
                    { MoviesState.Loaded(it) }
                )
            )
        }
    }

    val movies: LiveData<ScreenState> = moviesData
}