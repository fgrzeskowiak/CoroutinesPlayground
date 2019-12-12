package com.example.coroutinesplayground.basic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coroutinesplayground.R
import com.example.coroutinesplayground.common.*
import kotlinx.android.synthetic.main.activity_movies.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.androidx.scope.currentScope
import org.koin.core.parameter.parametersOf

class BasicMoviesActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, BasicMoviesActivity::class.java)
    }

    private val presenter: BasicMoviesPresenter by viewModel()
    private val dataAdapter: BaseAdapter by currentScope.inject {
        parametersOf(lifecycleScope)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_movies)

        movies_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dataAdapter
        }

        presenter.movies.observe(this) {
            when (it) {
                ScreenState.Init -> displayProgress()
                is MoviesState.Loaded -> displaySuccess(it.items)
                is ScreenState.Failed -> displayError(it.error)
            }
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
}