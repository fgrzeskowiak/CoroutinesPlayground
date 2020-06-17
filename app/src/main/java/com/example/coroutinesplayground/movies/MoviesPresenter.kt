package com.example.coroutinesplayground.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import arrow.core.Left
import arrow.core.Right
import arrow.core.flatMap
import com.example.coroutinesplayground.common.*
import com.example.coroutinesplayground.util.flatMapMergeRight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlin.coroutines.coroutineContext

class MoviesPresenter(private val moviesDao: MoviesDao) : ViewModel() {

    private val movieSelected = SingleLiveData<Int>()

    interface View {
        val textChangesFlow: Flow<String>
    }

    val clicked: LiveData<Int> = movieSelected

    val moviesWithView: (View) -> LiveData<ScreenState> = { view ->
        moviesDao.moviesResponse
            .onEach { println("CoroutineContext moviesResponse $coroutineContext") }
            .flatMapMergeRight { movies ->
                view.textChangesFlow
                    .debounce(300)
                    .onStart { emit("") }
                    .onEach { println("CoroutineContext textChangesFlow: $coroutineContext") }
                    .map { query ->
                        movies.results
                            .filter { it.title.contains(query) }
                            .map { MovieAdapterItem(it.id, it.title, movieSelected) }
                    }
                    .flowOn(Dispatchers.IO)
            }
            .map { moviesEither -> moviesEither.flatMap { if (it.isEmpty()) Left(EmptyListError) else Right(it) } }
            .flowOn(Dispatchers.Default)
            .map { it.fold({ ScreenState.Failed(it) }, { MoviesState.Loaded(it) }) }
            .onStart { emit(ScreenState.Init) }
            .onEach { println("CoroutineContext moviesFlow: $coroutineContext") }
            .asLiveData()
    }
}

data class MovieAdapterItem(val id: Int, val title: String, val clickData: MediatorLiveData<Int>) :
    KotlinBaseAdapterItem<Int> {

    override fun itemId(): Int = id

    fun onClick() {
        clickData.value = id
    }
}