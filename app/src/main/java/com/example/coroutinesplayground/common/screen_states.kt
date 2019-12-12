package com.example.coroutinesplayground.common

import com.example.coroutinesplayground.models.MovieData

sealed class ScreenState {
    object Init : ScreenState()
    data class Failed(val error: DefaultError) : ScreenState()
}

sealed class MoviesState : ScreenState() {
    data class Loaded(val items: List<BaseAdapterItem>) : MoviesState()
}

sealed class MovieDetailsState: ScreenState() {
    data class Loaded(val movieData: MovieData): MovieDetailsState()
}