package com.example.coroutinesplayground.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import arrow.core.extensions.option.monad.flatten
import arrow.core.maybe
import arrow.core.toOption
import coil.api.load
import com.example.coroutinesplayground.R
import com.example.coroutinesplayground.common.DefaultError
import com.example.coroutinesplayground.common.MovieDetailsState
import com.example.coroutinesplayground.common.ScreenState
import com.example.coroutinesplayground.common.toMessage
import com.example.coroutinesplayground.models.MovieData
import com.example.coroutinesplayground.view.navigationClicks
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MovieDetailsActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ID = "extra_id"
        fun newIntent(context: Context, id: Int) = Intent(context, MovieDetailsActivity::class.java)
            .putExtra(EXTRA_ID, id)
    }

    private val presenter: MovieDetailsPresenter by viewModel {
        parametersOf(intent.hasExtra(EXTRA_ID).maybe {
            intent.extras?.getInt(EXTRA_ID).toOption()
        }.flatten())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_details)
        super.onCreate(savedInstanceState)

        presenter.withView(object : MovieDetailsPresenter.View {
            override val navigationClickFlow: Flow<Unit> = movie_details_toolbar.navigationClicks()
        }).observe(this) {
            finish()
        }

        presenter.movieStateData.observe(this) {
            when (it) {
                is MovieDetailsState.Loaded -> showContent(it.movieData)
                ScreenState.Init -> showProgress()
                is ScreenState.Failed -> showError(it.error)
            }
        }
    }

    private fun showProgress() {
        movie_details_progress.visibility = View.VISIBLE
        movie_details_content_layout.visibility = View.GONE
        movie_details_error.visibility = View.GONE
    }

    private fun showContent(movieData: MovieData) {
        movie_details_progress.visibility = View.GONE
        movie_details_content_layout.visibility = View.VISIBLE
        movie_details_image.load("https://image.tmdb.org/t/p/w500${movieData.posterUrl}") {
            crossfade(true)
        }
        movie_details_toolbar.title = movieData.title
        movie_details_overview.text = movieData.description
        movie_details_error.visibility = View.VISIBLE
    }

    private fun showError(error: DefaultError) {
        movie_details_progress.visibility = View.GONE
        movie_details_content_layout.visibility = View.GONE
        movie_details_error.visibility = View.VISIBLE
        movie_details_error.text = error.toMessage(resources)
    }
}