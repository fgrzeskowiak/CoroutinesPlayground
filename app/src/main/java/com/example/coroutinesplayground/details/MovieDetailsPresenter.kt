package com.example.coroutinesplayground.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Left
import arrow.core.Option
import com.example.coroutinesplayground.common.BaseError
import com.example.coroutinesplayground.common.MovieDetailsState
import com.example.coroutinesplayground.common.MovieIdError
import com.example.coroutinesplayground.common.ScreenState
import com.example.coroutinesplayground.models.MovieData
import com.example.coroutinesplayground.movies.MoviesDao
import kotlinx.coroutines.flow.*

class MovieDetailsPresenter(
    movieId: Option<Int>,
    moviesDao: MoviesDao
) : ViewModel() {

    interface View {
        val navigationClickFlow: Flow<Unit>
    }

    val movieStateData: LiveData<ScreenState> = movieId.fold(
        { flowOf(Left(MovieIdError)) },
        { moviesDao.movieDetailsResponse(it) }
    ).map {
        it.fold(
            { ScreenState.Failed(it) },
            {
                MovieDetailsState.Loaded(
                    MovieData(it.title, it.posterPath, it.overview)
                )
            }
        )
    }
        .onStart { ScreenState.Init }
        .asLiveData()

    val withView: (View) -> LiveData<Unit> = { view: View ->
        view.navigationClickFlow
            .asLiveData()
    }
}