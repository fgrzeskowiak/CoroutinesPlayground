package com.example.coroutinesplayground.movies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coroutinesplayground.*
import com.example.coroutinesplayground.common.*
import com.example.coroutinesplayground.common.toMessage
import com.example.coroutinesplayground.details.MovieDetailsActivity
import com.example.coroutinesplayground.view.textChanges
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.androidx.scope.currentScope
import org.koin.core.parameter.parametersOf

class MoviesActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, MoviesActivity::class.java)
    }

    private val dataAdapter: BaseAdapter by currentScope.inject {
        parametersOf(lifecycleScope)
    }
    private val presenter: MoviesPresenter by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        movies_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dataAdapter
        }

        presenter.moviesWithView(object : MoviesPresenter.View {
            override val textChangesFlow: Flow<String> = movies_query_input.textChanges()
        }).observe(this) {
            when (it) {
                ScreenState.Init -> displayProgress()
                is MoviesState.Loaded -> displaySuccess(it.items)
                is ScreenState.Failed -> displayError(it.error)
            }
        }

        presenter.clicked.observe(this) {
            startActivity(MovieDetailsActivity.newIntent(this@MoviesActivity, it))
        }
    }

    private fun displayProgress() {
        movies_recycler_view.visibility = View.GONE
        movies_progress.visibility = View.VISIBLE
        movies_error.visibility = View.GONE
    }

    private fun displaySuccess(items: List<BaseAdapterItem>) {
        movies_recycler_view.visibility = View.VISIBLE
        movies_progress.visibility = View.GONE
        movies_error.visibility = View.GONE
        dataAdapter.submitList(items)
    }

    private fun displayError(error: DefaultError) {
        movies_recycler_view.visibility = View.GONE
        movies_progress.visibility = View.GONE
        movies_error.visibility = View.VISIBLE
        movies_error.text = error.toMessage(resources)
    }

    override fun onDestroy() {
        super.onDestroy()
        movies_recycler_view.adapter = null
    }
}