package com.zkatemor.movies.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.zkatemor.movies.R
import com.zkatemor.movies.adapter.MovieAdapter
import com.zkatemor.movies.app.App
import com.zkatemor.movies.model.Movie
import com.zkatemor.movies.network.MoviesResponse
import com.zkatemor.movies.network.ResponseCallback
import com.zkatemor.movies.util.MoviesRepository
import com.zkatemor.movies.util.Tools
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Named

class MainActivity : BaseActivity() {
    @Inject
    @Named("Movies_Repository")
    lateinit var repository: MoviesRepository

    private val DIRECTION_UP: Int = -1
    private val BASE_IMAGE_URL: String = "https://image.tmdb.org/t/p/w500/"

    private var movies: ArrayList<Movie> = ArrayList()
    private var page : Int = 1
    private var isLoadData: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeScrollListenerOnRecView(LinearLayoutManager(this))

        App.component!!.injectsMainActivity(this)
        initializeData()
        initializeSwipeRefreshLayoutListener()
    }

    private fun initializeData() {
        visibleProgress()
        isLoadData = true
        addMovies()
    }

    private fun addMovies() {
        repository.getMovies(object : ResponseCallback<MoviesResponse> {
            override fun onSuccess(apiResponse: MoviesResponse) {
                if (page == 1)
                    movies = ArrayList()

                apiResponse.movies.forEach {
                    movies.add(
                        Movie(it.id, it.name, it.description, Tools.convertDate(it.date), BASE_IMAGE_URL + it.imageURL)
                    )
                }
                if (page > 1) {
                    rec_view_movie_card.adapter!!.notifyItemInserted(movies.size - 1)
                } else
                    setDataOnRecView()
                isLoadData = false
            }

            override fun onFailure(errorMessage: String) {
                isLoadData = false
            }
        }
            , page)
    }

    private fun setDataOnRecView() {
        val adapter = MovieAdapter(movies, this)
        val manager = LinearLayoutManager(this)
        rec_view_movie_card.layoutManager = manager
        rec_view_movie_card.adapter = adapter

        invisibleProgress()

        initializeScrollListenerOnRecView(manager)
    }

    private fun initializeScrollListenerOnRecView(manager: LinearLayoutManager) {
        rec_view_movie_card.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                swipe_refresh_layout.isEnabled = !(recyclerView?.canScrollVertically(DIRECTION_UP))

                val countVisible = manager.childCount
                val countGeneral = manager.itemCount
                val firstPosition = manager.findFirstVisibleItemPosition()

                if (!isLoadData /*&& isInternetAccess*/) {
                    if ((countVisible + firstPosition) >= countGeneral) {
                        page++
                        isLoadData = true
                        addMovies()
                    }
                }
            }
        })
    }

    private fun initializeSwipeRefreshLayoutListener() {
        swipe_refresh_layout.setOnRefreshListener {
            movies = ArrayList()
            page = 1
            isLoadData = true
            addMovies()
        }
    }

    private fun invisibleProgress() {
        swipe_refresh_layout.isRefreshing = false
        main_progress_bar.visibility = View.INVISIBLE
    }

    private fun visibleProgress() {
        main_progress_bar.visibility = View.VISIBLE
    }
}
