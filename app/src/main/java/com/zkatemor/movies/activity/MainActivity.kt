package com.zkatemor.movies.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import com.zkatemor.movies.R
import com.zkatemor.movies.adapter.MovieAdapter
import com.zkatemor.movies.app.App
import com.zkatemor.movies.model.Movie
import com.zkatemor.movies.network.MoviesResponse
import com.zkatemor.movies.network.ResponseCallback
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Named
import android.widget.Toast
import com.zkatemor.movies.util.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.io.Serializable

class MainActivity : BaseActivity() {
    @Inject
    @Named("Movies_Repository")
    lateinit var repository: MoviesRepository

    @Inject
    @Named("Search_Repository")
    lateinit var search_repository: SearchRepository

    lateinit var adapter: MovieAdapter

    private val BASE_IMAGE_URL: String = "https://image.tmdb.org/t/p/w500/"
    private val MOVIES_KEY = "movies"

    private var movies: ArrayList<Movie> = ArrayList()
    private var search_movies: ArrayList<Movie> = ArrayList()
    private var isLoadData: Boolean = true
    private var isSearch: Boolean = false
    private var movie: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.component!!.injectsMainActivity(this)

        initializeData()

        initializeSwipeRefreshLayoutListener()

        setOnClickUpdateButton()

        initializeSearchEditText()

        initializeData()
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.clear()
        savedInstanceState.putSerializable(MOVIES_KEY, movies as Serializable)
        super.onSaveInstanceState(savedInstanceState)
    }

    private fun initializeAdapter(data: ArrayList<Movie>) {
        adapter = MovieAdapter(data)
        adapter.onItemClick = { movie ->
            val toast = Toast.makeText(
                this,
                movie.getName, Toast.LENGTH_SHORT
            )
            toast.show()
        }

        adapter.onLikeClick = { like, movie ->
            if (like.drawable.constantState == resources.getDrawable(R.drawable.ic_heart_fill).constantState) {
                like.setImageResource(R.drawable.ic_heart)
                movie.isFavorites = false
                getPreferences().remove(movie.getId)
            } else {
                like.setImageResource(R.drawable.ic_heart_fill)
                movie.isFavorites = true
                getPreferences().save(movie.getId)
            }
        }
    }

    private fun initializeData() {
        invisibleNotFoundLayout()
        invisibleErrorLayout()
        visibleProgress()
        isLoadData = true
        addData()
    }

    private fun addMovies() {
        repository.getMovies(object : ResponseCallback<MoviesResponse> {
            override fun onSuccess(apiResponse: MoviesResponse) {
                movies = ArrayList()

                apiResponse.movies.forEach {
                    movies.add(
                        Movie(
                            it.id, it.name, it.description, Tools.convertDate(it.date),
                            BASE_IMAGE_URL + it.imageURL, getPreferences().isLiked(it.id)
                        )
                    )
                }
                setDataOnRecView(movies)
                isLoadData = false
                isSearch = false
            }

            override fun onFailure(errorMessage: String) {
                isLoadData = false
                visibleErrorLayout()
            }
        })
    }

    private fun searchMovies() {
        search_repository.searchMovies(object : ResponseCallback<MoviesResponse> {
            override fun onSuccess(apiResponse: MoviesResponse) {
                apiResponse.movies.forEach {
                    search_movies.add(
                        Movie(
                            it.id, it.name, it.description, Tools.convertDate(it.date),
                            BASE_IMAGE_URL + it.imageURL, getPreferences().isLiked(it.id)
                        )
                    )
                }
                if (search_movies.count() >= 1) {
                    setDataOnRecView(search_movies)
                    isLoadData = false
                } else {
                    visibleNotFoundLayout()
                }
            }

            override fun onFailure(errorMessage: String) {
                isSearch = false
                isLoadData = false
                visibleErrorLayout()
            }
        }
            , movie)
    }

    private fun visibleErrorLayout() {
        error_layout.visibility = View.VISIBLE
        linear_layout.visibility = View.INVISIBLE
        invisibleProgress()
    }

    private fun invisibleErrorLayout() {
        invisibleProgress()
        linear_layout.visibility = View.VISIBLE
        error_layout.visibility = View.INVISIBLE
    }

    private fun visibleNotFoundLayout() {
        not_found_layout.visibility = View.VISIBLE
        linear_layout.visibility = View.INVISIBLE
        not_found_text_view.text = "По запросу “" + movie + "” ничего не найдено"
        invisibleProgress()
    }

    private fun invisibleNotFoundLayout() {
        invisibleProgress()
        linear_layout.visibility = View.VISIBLE
        not_found_layout.visibility = View.INVISIBLE
    }

    private fun setDataOnRecView(data: ArrayList<Movie>) {
        val manager = LinearLayoutManager(this)
        rec_view_movie_card.layoutManager = manager

        rec_view_movie_card.removeAllViews()
        initializeAdapter(data)
        rec_view_movie_card.adapter = adapter

        invisibleProgress()
    }

    private fun addData() {
        if (isSearch)
            searchMovies()
        else
            addMovies()
    }

    private fun initializeSearchEditText() {
        setSupportActionBar(main_toolbar)

        search_bar_edit_text.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                search_movies = ArrayList()
                movie = search_bar_edit_text.text.toString()
                isSearch = true
                initializeData()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun initializeSwipeRefreshLayoutListener() {
        swipe_refresh_layout.setOnRefreshListener {
            if (isSearch)
                search_movies = ArrayList()
            else
                movies = ArrayList()
            visibleProgress()
            isLoadData = true
            addData()
        }
    }

    private fun setOnClickUpdateButton() {
        update_image_view.setOnClickListener {
            initializeData()
            invisibleProgress()
        }
    }

    private fun invisibleProgress() {
        swipe_refresh_layout.isRefreshing = false
        search_pbar.visibility = View.INVISIBLE
        main_progress_bar.visibility = View.INVISIBLE
    }

    private fun visibleProgress() {
        if (movies.count() >= 1 || search_movies.count() >= 1)
            search_pbar.visibility = View.VISIBLE
        else
            main_progress_bar.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (isSearch) {
            isSearch = false
            movie = ""
            search_bar_edit_text.text.clear()
            initializeData()
        } else
            super.onBackPressed()
    }
}
