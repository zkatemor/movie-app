package com.zkatemor.movies.model

import com.zkatemor.movies.app.App
import com.zkatemor.movies.essence.Movie
import com.zkatemor.movies.network.MoviesResponse
import com.zkatemor.movies.network.ResponseCallback
import com.zkatemor.movies.presenter.MainPresenter
import com.zkatemor.movies.util.*
import javax.inject.Inject
import javax.inject.Named

class MainModel(private val presenter: MainPresenter): Model {
    @Inject
    @Named("Movies_Repository")
    lateinit var repository: MoviesRepository

    @Inject
    @Named("Search_Repository")
    lateinit var search_repository: SearchRepository

    init {
        App.component!!.injectsMainModel(this)
    }

    override fun addAllMovies(dataLoadStatus: MainPresenter.DataLoadStatus) {
        repository.getMovies(object : ResponseCallback<MoviesResponse> {
            override fun onSuccess(apiResponse: MoviesResponse) {
                val movies = ArrayList<Movie>()

                apiResponse.movies.forEach {
                    movies.add(
                        Movie(
                            it.id, it.name, it.description, Tools.convertDate(it.date),
                            Tools.buildImageURL(it.imageURL)
                        )
                    )
                }
                presenter.onSuccess(movies, dataLoadStatus)
            }
            override fun onFailure() = presenter.onFailure(dataLoadStatus)
        })
    }

    override fun searchMovies(movie: String) {
        val dataLoadStatus = MainPresenter.DataLoadStatus.Searching
        val search_movies = ArrayList<Movie>()

        search_repository.searchMovies(object : ResponseCallback<MoviesResponse> {
            override fun onSuccess(apiResponse: MoviesResponse) {
                apiResponse.movies.forEach {
                    search_movies.add(
                        Movie(
                            it.id, it.name, it.description, Tools.convertDate(it.date),
                            Tools.buildImageURL(it.imageURL)
                        )
                    )
                }
                if (search_movies.count() >= 1) {
                    presenter.onSuccess(search_movies, dataLoadStatus)
                } else {
                    presenter.onEmpty(movie)
                }
            }
            override fun onFailure() = presenter.onFailure(dataLoadStatus)
        }, movie)
    }
}