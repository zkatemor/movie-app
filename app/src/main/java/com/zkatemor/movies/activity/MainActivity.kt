package com.zkatemor.movies.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.zkatemor.movies.R
import com.zkatemor.movies.adapter.MovieAdapter
import com.zkatemor.movies.model.Movie
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val movies: ArrayList<Movie> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movies.add(Movie(0, "bvfo", "dgr", "ergreg", ""))
        rec_view_movie_card.layoutManager = LinearLayoutManager(this)
        rec_view_movie_card.adapter = MovieAdapter(movies, this@MainActivity)
    }
}
