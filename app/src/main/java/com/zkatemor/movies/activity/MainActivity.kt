package com.zkatemor.movies.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import com.zkatemor.movies.R
import com.zkatemor.movies.adapter.MovieAdapter
import com.zkatemor.movies.app.App
import com.zkatemor.movies.model.Movie
import com.zkatemor.movies.network.MoviesResponse
import com.zkatemor.movies.network.ResponseCallback
import com.zkatemor.movies.util.MoviesRepository
import com.zkatemor.movies.util.SearchRepository
import com.zkatemor.movies.util.Tools
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Named
import android.widget.Toast
import kotlinx.android.synthetic.main.toolbar_main.*

class MainActivity : BaseActivity() {
    @Inject
    @Named("Movies_Repository")
    lateinit var repository: MoviesRepository

    @Inject
    @Named("Search_Repository")
    lateinit var search_repository: SearchRepository

    private val DIRECTION_UP: Int = -1
    private val BASE_IMAGE_URL: String = "https://image.tmdb.org/t/p/w500/"

    private var movies: ArrayList<Movie> = ArrayList()
    private var search_movies: ArrayList<Movie> = ArrayList()
    private var page: Int = 1
    private var isLoadData: Boolean = true
    private var isSearch: Boolean = false
    private var movie: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeScrollListenerOnRecView(LinearLayoutManager(this))

        App.component!!.injectsMainActivity(this)

        initializeData()

        initializeSwipeRefreshLayoutListener()

        setOnClickUpdateButton()
    }

    private fun initializeData() {
        invisibleErrorLayout()
        visibleProgress()
        isLoadData = true
        if (!isSearch) addMovies()
        else searchMovies()
    }

    private fun addMovies() {
        repository.getMovies(object : ResponseCallback<MoviesResponse> {
            override fun onSuccess(apiResponse: MoviesResponse) {
                if (page == 1)
                    movies = ArrayList()

                apiResponse.movies.forEach {
                    movies.add(
                        Movie(
                            it.id, it.name, it.description, Tools.convertDate(it.date),
                            BASE_IMAGE_URL + it.imageURL, false
                        )
                    )
                }
                if (page > 1) {
                    rec_view_movie_card.adapter!!.notifyItemInserted(movies.size - 1)
                } else
                    setDataOnRecView(movies)
                isLoadData = false
                isSearch = false
            }

            override fun onFailure(errorMessage: String) {
                isLoadData = false
                visibleErrorLayout()
            }
        }
            , page)
    }

    private fun searchMovies() {
        search_repository.searchMovies(object : ResponseCallback<MoviesResponse> {
            override fun onSuccess(apiResponse: MoviesResponse) {
                apiResponse.movies.forEach {
                    search_movies.add(
                        Movie(
                            it.id, it.name, it.description, Tools.convertDate(it.date),
                            BASE_IMAGE_URL + it.imageURL, false
                        )
                    )
                }

                if (search_movies.count() >= 1) {
                    setDataOnRecView(search_movies)
                    isLoadData = false
                    isSearch = true
                } else {

                }
            }

            override fun onFailure(errorMessage: String) {
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

    private fun setDataOnRecView(data: ArrayList<Movie>) {
        val adapter = MovieAdapter(data)
        val manager = LinearLayoutManager(this)
        rec_view_movie_card.layoutManager = manager
        rec_view_movie_card.adapter = adapter

        adapter.onItemClick = { movie ->
            val toast = Toast.makeText(
                this,
                movie.getName, Toast.LENGTH_SHORT
            )
            toast.show()
        }

        invisibleProgress()

        initializeScrollListenerOnRecView(manager)

        initializeSearchEditText()
    }

    private fun initializeScrollListenerOnRecView(manager: LinearLayoutManager) {
        rec_view_movie_card.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                swipe_refresh_layout.isEnabled = !(recyclerView?.canScrollVertically(DIRECTION_UP))

                val countVisible = manager.childCount
                val countGeneral = manager.itemCount
                val firstPosition = manager.findFirstVisibleItemPosition()

                if (!isLoadData) {
                    if ((countVisible + firstPosition) >= countGeneral) {
                        page++
                        isLoadData = true
                        addMovies()
                    }
                }
            }
        })
    }

    private fun initializeSearchEditText() {
        setSupportActionBar(main_toolbar)

        search_bar_edit_text.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                search_movies = ArrayList()
                movie = search_bar_edit_text.text.toString()
                searchMovies()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun initializeSwipeRefreshLayoutListener() {
        swipe_refresh_layout.setOnRefreshListener {
            movies = ArrayList()
            page = 1
            isLoadData = true
            initializeData()
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
        main_progress_bar.visibility = View.INVISIBLE
    }

    private fun visibleProgress() {
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
