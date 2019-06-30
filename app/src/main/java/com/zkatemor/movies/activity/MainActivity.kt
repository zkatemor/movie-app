package com.zkatemor.movies.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import com.zkatemor.movies.R
import com.zkatemor.movies.adapter.MovieAdapter
import com.zkatemor.movies.app.App
import com.zkatemor.movies.essence.Movie
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import android.widget.Toast
import com.zkatemor.movies.presenter.MainPresenter
import com.zkatemor.movies.util.*
import com.zkatemor.movies.view.MainView
import kotlinx.android.synthetic.main.toolbar_main.*
import java.io.Serializable

class MainActivity : AppCompatActivity(), MainView {
    @Inject
    lateinit var presenter: MainPresenter

    private var movies: ArrayList<Movie> = ArrayList()
    private var search_movies: ArrayList<Movie> = ArrayList()

    lateinit var adapter: MovieAdapter
    lateinit var manager: LinearLayoutManager
    private val RECYCLER_POSITION_KEY = "recycler_position"
    private val SEARCH_FLAG_KEY = "is_search"
    private val DATA_KEY = "data"
    private val SEARCH_MOVIE = "search_movie"

    private var isSearch: Boolean = false
    private var movie: String = ""
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        manager = LinearLayoutManager(this)

        App.component!!.injectsMainActivity(this)

        presenter.create(this)
        getData(savedInstanceState)
        initializeSwipeRefreshLayoutListener()
        setOnClickUpdateButton()
        initializeSearchEditText()
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.clear()
        if (!isSearch)
            savedInstanceState.putSerializable(DATA_KEY, movies as Serializable)
        else
            savedInstanceState.putSerializable(DATA_KEY, search_movies as Serializable)
        savedInstanceState.putInt(RECYCLER_POSITION_KEY, manager.findFirstCompletelyVisibleItemPosition())
        savedInstanceState.putBoolean(SEARCH_FLAG_KEY, isSearch)
        savedInstanceState.putString(SEARCH_MOVIE, movie)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun hasContent(): Boolean = (movies.size > 0 || search_movies.size > 0)

    override fun showMainProgressBar() {
        main_progress_bar.visibility = View.VISIBLE
    }

    override fun showErrorLayout() {
        visibleErrorLayout()
    }

    override fun showFindEmpty(movie: String) {
        visibleNotFoundLayout()
    }

    override fun showSwipeRefresh() {
        swipe_refresh_layout.visibility = View.VISIBLE
    }

    override fun showMovies(data: ArrayList<Movie>) {
        setDataOnRecView(data)
        if (isSearch)
            search_movies = data
        else
            movies = data
    }

    override fun showProgressBar() {
        search_pbar.visibility = View.VISIBLE
    }

    override fun hideMainProgressBar() {
        main_progress_bar.visibility = View.INVISIBLE
    }

    override fun hideProgressBar() {
        search_pbar.visibility = View.INVISIBLE
    }

    override fun showConnectivityError() {
        invisibleProgress()
        val error_snackbar = SnackBar(main_layout)
        error_snackbar.show(this)
    }

    private fun getData(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            initializeData()
        else {
            val data = savedInstanceState.getSerializable(DATA_KEY) as ArrayList<Movie>
            position = savedInstanceState.getInt(RECYCLER_POSITION_KEY)
            isSearch = savedInstanceState.getBoolean(SEARCH_FLAG_KEY)
            movie = savedInstanceState.getString(SEARCH_MOVIE)

            if (isSearch)
                search_movies = data
            else movies = data

            if (position == RecyclerView.NO_POSITION)
                position = 0

            setDataOnRecView(data)
            rec_view_movie_card.smoothScrollToPosition(position)
        }
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
        addData()
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
        rec_view_movie_card.layoutManager = manager

        rec_view_movie_card.removeAllViews()
        initializeAdapter(data)
        rec_view_movie_card.adapter = adapter

        invisibleProgress()
    }

    private fun addData() {
        if (isSearch)
            presenter.searchMovies(movie)
        else
            presenter.addMovies()
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
            presenter.updateMovies(isSearch, movie)
        }
    }

    private fun setOnClickUpdateButton() {
        update_image_view.setOnClickListener {
            visibleProgress()
            initializeData()
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
            visibleProgress()
            setDataOnRecView(movies)
        } else
            super.onBackPressed()
    }
}