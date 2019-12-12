package com.example.coroutinesplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutinesplayground.basic.BasicMoviesActivity
import com.example.coroutinesplayground.movies.MoviesActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        basic_movie_list.setOnClickListener {
            startActivity(BasicMoviesActivity.newIntent(this))
        }

        movie_list.setOnClickListener {
            startActivity(MoviesActivity.newIntent(this))
        }
    }
}
